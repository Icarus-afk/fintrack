# FinTrack DTO Layer – Deep Technical Documentation

This document provides an in-depth explanation of the Data Transfer Object (DTO) layer in the FinTrack backend, including its design rationale, structure, mapping strategies, and the role of each DTO in the API contract. It is intended for developers extending, maintaining, or integrating with the FinTrack backend.

---

## 1. Purpose and Philosophy of the DTO Layer

- **Encapsulation:** DTOs decouple the internal JPA entity model from the API, preventing accidental exposure of sensitive fields and allowing the domain model to evolve independently of the API contract.
- **Validation:** DTOs are the primary place for request validation (e.g., using Jakarta Bean Validation annotations like `@NotNull`, `@Email`, `@Size`).
- **Documentation:** DTOs define the OpenAPI/Swagger schemas, ensuring the API is self-documenting and consistent.
- **Immutability:** All DTOs are Java records, making them immutable, thread-safe, and concise.
- **Mapping:** Dedicated mappers convert between entities and DTOs, centralizing transformation logic and supporting derived fields (e.g., budget utilization, net balance).

---

## 2. DTO Layer Structure

The DTO layer is organized by feature, with each subpackage containing request and response records for a specific domain area. All DTOs are implemented as Java records for immutability and concise syntax.

### Package Overview

```
dto/
  auth/             // Authentication and token flows
  budget/           // Budget CRUD and summaries
  common/           // API envelope, error, and pagination metadata
  mapper/           // Entity-to-DTO mapping utilities
  notification/     // Notification responses and bulk updates
  recurring/        // Recurring job creation/updates
  sharedwallet/     // Shared wallet and member management
  transaction/      // Transaction CRUD, filters, summaries
  user/             // User profile, password, avatar, 2FA
```

---

## 3. Detailed DTO Breakdown

### 3.1 `dto.common` – API Envelope and Metadata
- **ApiResponse<T>**: Generic wrapper for all API responses. Fields:
  - `success` (boolean): Indicates if the request was successful.
  - `data` (T): The actual response payload (null on error).
  - `error` (ErrorDetail): Error details (null on success).
  - `meta` (ResponseMeta): Metadata (timestamp, requestId, pagination).
- **ErrorDetail**: Contains error code, message, and optional details map for validation errors or context.
- **ResponseMeta**: Holds timestamp, requestId, and optional `PaginationMeta` for paginated endpoints.
- **PaginationMeta**: Page index, size, total elements, and total pages for paginated responses.

### 3.2 `dto.auth` – Authentication and Security
- **LoginRequest**: `{ email, password }` for login.
- **RegisterRequest**: `{ fullName, email, password }` for registration.
- **TokenResponse**: `{ accessToken, refreshToken, expiresInSeconds }` for token issuance.
- **ForgotPasswordRequest, ResetPasswordRequest**: For password reset flows.
- **TwoFactorVerificationRequest**: `{ email, code }` for 2FA verification.
- **LogoutRequest, RefreshTokenRequest**: For token lifecycle management.

### 3.3 `dto.user` – User Profile and Security
- **UserProfileResponse**: `{ id, fullName, email, role, avatarUrl, twoFactorEnabled, createdAt, updatedAt }` for `/users/me`.
- **UpdateProfileRequest**: `{ fullName, avatarUrl }` for profile updates.
- **ChangePasswordRequest**: `{ currentPassword, newPassword }` for password changes.
- **AvatarUploadResponse**: `{ fileName, url, size, contentType }` for avatar uploads.
- **TwoFactorSetupResponse**: `{ secret, qrCodeUrl }` for 2FA setup.

### 3.4 `dto.transaction` – Transactions
- **TransactionResponse**: Full transaction details, including userId, sharedWalletId, title, amount, currency, category, type, eventDate, note, attachmentUrl, createdAt, updatedAt.
- **CreateTransactionRequest, UpdateTransactionRequest**: For creating/updating transactions. All fields except IDs.
- **TransactionFilter**: `{ from, to, categories, type, minAmount, maxAmount, sharedWalletId }` for advanced filtering.
- **TransactionSummaryResponse**: `{ totalIncome, totalExpense, netBalance }` for dashboard summaries.

### 3.5 `dto.budget` – Budgets
- **BudgetResponse**: `{ id, userId, month, category, amount, usedAmount, alertThreshold, remainingAmount, percentageUsed, createdAt, updatedAt }`.
- **CreateBudgetRequest, UpdateBudgetRequest**: For creating/updating budgets.

### 3.6 `dto.notification` – Notifications
- **NotificationResponse**: `{ id, userId, type, title, message, metadata, read, createdAt, updatedAt }`.
- **MarkNotificationsReadRequest**: `{ notificationIds }` for bulk mark-as-read.

### 3.7 `dto.recurring` – Recurring Jobs
- **RecurringJobResponse**: `{ id, userId, templateTransactionId, frequency, nextRunAt, lastRunAt, active, createdAt, updatedAt }`.
- **CreateRecurringJobRequest, UpdateRecurringJobRequest**: For scheduling and updating recurring jobs.

### 3.8 `dto.sharedwallet` – Shared Wallets
- **SharedWalletResponse**: `{ id, name, ownerId, members, createdAt, updatedAt }`.
- **SharedWalletMemberResponse**: `{ id, walletId, memberId, memberName, admin, shareRatio, runningBalance }`.
- **CreateSharedWalletRequest, UpdateSharedWalletRequest**: For wallet creation and updates.
- **SharedWalletMemberUpsertRequest, UpdateSharedWalletMemberRequest, AddSharedWalletMemberRequest**: For managing wallet members.

### 3.9 `dto.mapper` – Entity-to-DTO Mapping
- **UserMapper**: Converts `User` entity to `UserProfileResponse`.
- **TransactionMapper**: Handles entity <-> DTO for transactions, including summary calculations.
- **BudgetMapper**: Maps budgets and computes utilization/remaining.
- **NotificationMapper**: Maps notifications.
- **RecurringJobMapper**: Maps recurring jobs.
- **SharedWalletMapper**: Maps wallets and members, defaulting nulls to safe values.

---

## 4. Mapping and Validation Strategy

- **Mapping**: All mapping logic is centralized in `dto.mapper` classes. These are stateless, static utility classes. They:
  - Convert entities to DTOs for API responses.
  - Convert DTOs to entities for persistence (on create/update).
  - Compute derived fields (e.g., budget percentage used, net balance).
  - Default nulls to safe values (e.g., BigDecimal.ZERO).
- **Validation**: Request DTOs are the right place for validation annotations (e.g., `@NotNull`, `@Email`, `@Size`). This ensures invalid requests are rejected before reaching business logic.
- **Immutability**: All DTOs are records, so they are immutable and thread-safe by design.

---

## 5. Usage Patterns

- **Controllers**: Accept request DTOs and return response DTOs wrapped in `ApiResponse`.
- **Services**: Operate on entities, using mappers to convert to/from DTOs.
- **Error Handling**: All errors are returned in the standardized envelope with `ErrorDetail`.
- **Pagination**: Paginated endpoints use `PaginationMeta` in the response meta.
- **Bulk Operations**: DTOs like `MarkNotificationsReadRequest` and `SharedWalletMemberUpsertRequest` support efficient batch updates.

---

## 6. Best Practices and Extensibility

- **Never expose entities directly** in API responses.
- **Add new DTOs** for new endpoints or changes in contract, versioning as needed.
- **Keep mapping logic out of controllers/services**; use mappers for all transformations.
- **Document DTOs** with JavaDoc for clarity and OpenAPI generation.
- **Validate** all incoming requests at the DTO level.

---

## 7. Example: Transaction API Flow

1. **Client** sends a `CreateTransactionRequest` JSON payload to `/api/v1/transactions`.
2. **Controller** receives the DTO, validates it, and passes it to the service.
3. **Service** uses `TransactionMapper.toEntity()` to create a `Transaction` entity, persists it, then uses `TransactionMapper.toResponse()` to build a `TransactionResponse` DTO.
4. **Controller** wraps the response in `ApiResponse<TransactionResponse>` and returns it to the client.

---

## 8. Summary

The DTO layer in FinTrack is a robust, maintainable, and extensible foundation for API contracts. It ensures:
- Security (no entity leakage)
- Consistency (standardized envelopes)
- Flexibility (easy to evolve)
- Testability (clear boundaries)

For further details, see the JavaDoc in each DTO or the main README.
