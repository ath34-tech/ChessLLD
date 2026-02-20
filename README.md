# Chess Game — Low-Level Design (LLD)

A low-level design for a **two-player chess game** played on a standard **8×8 (64-cell) board**, supporting **legal move validation**, **captures**, **check/checkmate**, and key **special moves** such as **castling** and **en passant**.

---

## 1. Problem Statement

Design a software system that enables **two users to play chess** with full rule enforcement. The system must maintain game state, validate moves, handle turns, and determine end conditions such as checkmate, stalemate, and draw scenarios.

---

## 2. Scope

### In scope
- Two-player chess game (White vs Black)
- 8×8 board, 6 piece types
- Turn-based play
- Legal move validation including:
  - piece movement rules
  - path blocking (for sliding pieces)
  - captures
  - king safety (no self-check)
  - pinned pieces restrictions (cannot expose king)
- Special moves:
  - Castling
  - En passant
- Game end conditions:
  - Checkmate
  - Stalemate
  - Draw by insufficient material
  - Draw by 50-move rule

### Out of scope (can be added later)
- UI/Frontend
- Online matchmaking / networking
- Clocks / timers
- PGN/FEN import-export
- Threefold repetition (not listed in requirements, optional future enhancement)

---

## 3. Functional Requirements

1. Two users can play a game.
2. The game contains 6 piece types: **Rook, Knight, Bishop, Queen, King, Pawn**.
3. One board exists per game with **8×8 squares**.
4. Players make moves alternately.
5. Only **legal** moves are allowed.
6. Piece movement rules:
   - Rook: horizontal or vertical any distance
   - Knight: L-shaped move
   - Bishop: diagonal any distance
   - King: one step in any direction
   - Queen: any direction any distance
   - Pawn: forward move (1, or 2 on first move), diagonal capture
7. A move is legal only if:
   - the moving piece is not pinned in a way that exposes its king
   - the move does not leave its own king in check
   - the destination square is reachable and valid
   - the path is not blocked for sliding pieces
8. Captures:
   - capture must be consistent with the piece’s capture rules
   - captured piece is removed from the board
   - attacker occupies the destination square
9. Special moves:
   - En passant
   - Castling
10. Castling is disallowed if:
   - the king is currently in check
   - any square the king passes through is under attack
   - any piece exists between king and rook
11. Checkmate:
   - king is in check AND no legal move exists to escape check
12. Stalemate:
   - not in check AND no legal move exists
13. Draw by insufficient material.
14. Draw by 50-move rule:
   - 50 full moves without any capture or pawn move (commonly defined that way; if you strictly want “capture or check”, update rule accordingly)

---

## 4. High-Level Architecture

**Core components**
- `Game`: owns the board, players, current turn, and status.
- `Board`: owns an 8×8 grid of `Cell`.
- `Cell`: represents a square; may contain a `Piece` or be empty.
- `Piece` hierarchy: encapsulates movement rules.
- `Move`: immutable record of one attempted/executed move.
- `MoveValidator` (or `RulesEngine`): validates legality of a move in context of board + king safety.
- `GameHistory`: stores executed moves for auditing, draw checks, and special-move context (e.g., en passant eligibility).

---

## 5. Core Entities and Responsibilities

### 5.1 Player (User)
**Attributes**
- `name: String`
- `color: Color` (WHITE / BLACK)
- `moves: List<Move>` (or derived from game history)
- `result: Result` (WON / LOST / DRAW / ONGOING)
- `capturedPieces: List<Piece>`

**Responsibilities**
- Provides input move intent (from UI layer)
- Can be associated with moves executed in game

---

### 5.2 Game
**Attributes**
- `gameId: String`
- `board: Board`
- `players: Map<Color, Player>`
- `turn: Color`
- `status: GameStatus` (IN_PROGRESS / CHECK / CHECKMATE / STALEMATE / DRAW / RESIGNED)
- `history: GameHistory`
- `rules: RulesEngine`

**Responsibilities**
- Initialize and start game
- Enforce turn alternation
- Accept a move request, validate it, apply it
- Update status after every move:
  - check, checkmate, stalemate, draw

---

### 5.3 Board
**Attributes**
- `cells: Cell[8][8]`

**Responsibilities**
- Initialize 64 cells
- Place initial 32 pieces
- Provide queries:
  - get piece at position
  - check empty/occupied
  - apply move (with capture)

---

### 5.4 Cell
**Attributes**
- `position: Position`
- `piece: Piece | null`

**Responsibilities**
- Stores occupancy and piece reference
- Lightweight helper for board access

---

### 5.5 Position
**Attributes**
- `row: int` (0–7)
- `col: int` (0–7)

**Responsibilities**
- Identifies a square
- Utility operations (delta, bounds check)

---

### 5.6 Move
**Attributes**
- `player: Player`
- `from: Position`
- `to: Position`
- `piece: Piece`
- `captured: Piece | null`
- `moveType: MoveType` (NORMAL / CAPTURE / CASTLE / EN_PASSANT / PROMOTION)
- `timestamp: Instant` (optional)

**Responsibilities**
- Represents a single move attempt/execution
- Stored in history for replay and rule checks

---

### 5.7 GameHistory
**Attributes**
- `moves: List<Move>`

**Responsibilities**
- Append moves
- Provide last move (needed for en passant)
- Support draw checks:
  - 50-move rule counters
  - insufficient material evaluation

---

## 6. Piece Model

### 6.1 Piece (Interface / Abstract Class)
**Common attributes**
- `color: Color`
- `position: Position`
- `alive: boolean`

**Common responsibilities**
- Provide candidate moves ignoring king-safety:
  - `getPseudoLegalMoves(board): List<Position>`
- Piece identity:
  - `getType(): PieceType`

> Note: Final legality is determined by `RulesEngine` because legality depends on *game state* (pins, check, castling constraints, etc.).

---

### 6.2 Concrete Pieces

#### Rook
- Moves horizontally/vertically any number of squares
- Cannot jump; path must be clear

#### Bishop
- Moves diagonally any number of squares
- Cannot jump; path must be clear

#### Queen
- Combines rook + bishop movement
- Cannot jump; path must be clear

#### Knight
- L-shaped (2+1)
- Can jump over pieces

#### King
- One step in any direction
- Subject to “cannot move into check”
- Castling is handled by rules engine

#### Pawn
- Forward 1 square (if empty)
- Forward 2 squares from initial rank (if both squares empty)
- Captures diagonally
- En passant supported (context-dependent)
- Promotion (optional if you want full chess; not in your requirements, but typically included)

---

## 7. Rules and Validation (RulesEngine / Validator)

### 7.1 Primary API
- `validateMove(game: Game, moveRequest): ValidationResult`
- `applyMove(game: Game, move: Move): void`

### 7.2 Validation checklist
A move is valid if all are true:

1. **Turn correctness**  
   The moving player color equals `game.turn`.

2. **Piece ownership**  
   Selected piece belongs to current player.

3. **Movement rule**  
   Destination is in piece pseudo-legal moves.

4. **Path blocking** (rook/bishop/queen)  
   All squares between source and destination are empty.

5. **Destination constraints**
   - Destination is empty OR occupied by opponent (capture)
   - Destination is not occupied by same color

6. **King safety**
   - Simulate the move
   - Ensure own king is not in check after move  
   This prevents illegal moves due to:
   - pinned pieces
   - moving king into attacked square
   - exposing king to line attacks

7. **Special moves**
   - **Castling**:
     - King and rook have not moved
     - Squares between king and rook are empty
     - King not in check
     - Squares king crosses and lands on are not under attack
   - **En passant**:
     - Previous move was opponent pawn moving two squares
     - Capture square matches en passant rules
     - Captured pawn is removed from its adjacent square
     - King safety must still hold after capture

---

## 8. Game End Conditions

### 8.1 Check
- After each move, determine if opponent king is attacked.

### 8.2 Checkmate
- King is in check AND no legal move exists for the checked player.

### 8.3 Stalemate
- King is not in check AND no legal move exists.

### 8.4 Draw conditions
- **Insufficient material** (examples):
  - King vs King
  - King + Bishop vs King
  - King + Knight vs King
  - (and other standard insufficient sets)
- **50-move rule**
  - track half-moves since last pawn move or capture (recommended standard)
  - declare draw when counter reaches threshold

---

## 9. Key Methods (Suggested)

### Initialization
- `Game.initialize(players)`
- `Board.initializeEmptyCells()`
- `Board.initializePieces()`

### Gameplay
- `Game.makeMove(moveRequest)`
	- build `Move`
	- `rules.validateMove(...)`
	- `rules.applyMove(...)`
	- `history.add(move)`
	- update game state (check/checkmate/stalemate/draw)
	- switch turn

### Validation / Status
- `RulesEngine.isKingInCheck(color): boolean`
- `RulesEngine.hasAnyLegalMove(color): boolean`
- `RulesEngine.isCheckmate(color): boolean`
- `RulesEngine.isStalemate(color): boolean`
- `RulesEngine.isDrawByInsufficientMaterial(): boolean`
- `RulesEngine.isDrawByFiftyMoveRule(): boolean`

---

## 10. Suggested Enums / Value Objects

- `Color { WHITE, BLACK }`
- `PieceType { KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN }`
- `GameStatus { IN_PROGRESS, CHECK, CHECKMATE, STALEMATE, DRAW }`
- `MoveType { NORMAL, CAPTURE, CASTLE, EN_PASSANT, PROMOTION }`

---

## 11. Notes / Implementation Guidance

- Prefer **immutability** for `Move` and `Position`.
- Prefer a **single validation pipeline**:
  - pseudo-legal move generation in pieces
  - full legality in rules engine via simulation
- For simulation:
  - either clone board state, or apply and rollback changes safely
- Keep rule-dependent state in history:
  - whether king/rooks moved (for castling)
  - last move (for en passant)
  - halfmove clock (for 50-move rule)

---

## 12. Future Enhancements (Optional)
- Promotion workflow (piece selection)
- Threefold repetition draw
- Move notation (SAN/PGN)
- FEN board serialization
- Time controls and resign/offer draw actions