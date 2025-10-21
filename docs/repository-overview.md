# Repository Layer in FinTrack: Technical Overview

This document explains the role of repositories in the FinTrack backend, how they work in a Spring Boot application, and details the specific repository interfaces implemented in your project.

---

## What Are Repositories?

In the context of Spring Boot and Java, a **repository** is a design pattern and a core part of the persistence layer. Repositories abstract the data access logic, providing a clean interface for querying, saving, updating, and deleting entities from the database. They:

- Hide the details of data storage and retrieval (SQL, JPA, etc.)
- Allow you to write expressive, type-safe queries using method names or annotations
- Enable easy unit testing and mocking of data access
- Promote separation of concerns: business logic stays in services, persistence logic in repositories

Spring Data JPA provides the `JpaRepository` interface, which offers a rich set of CRUD operations and query derivation from method names.

---

## How Do Repositories Work in Spring Boot?

- **Interface-based**: You define a Java interface (e.g., `UserRepository`) that extends `JpaRepository<Entity, ID>`.
- **Automatic implementation**: At runtime, Spring Boot generates the actual implementation for you.
- **Query methods**: You can declare methods like `findByEmail(String email)` and Spring will generate the SQL/JPA query automatically.
- **Custom queries**: For complex cases, you can use `@Query` annotations with JPQL or native SQL.

---

## What Do Repositories Do?

- **CRUD Operations**: Save, update, delete, and fetch entities.
- **Querying**: Find entities by various fields, with support for pagination, sorting, and filtering.
- **Transactions**: Work within Spring's transaction management for data consistency.
- **Abstraction**: Decouple the rest of your code from the details of the database.

---

## Repositories in Your Project

Your project defines a repository interface for each main entity. Each interface extends `JpaRepository`, inheriting all basic CRUD methods and adding custom queries as needed.

### 1. `UserRepository`
- **Entity:** `User`
- **Purpose:** Manage user accounts and lookups.
- **Custom Methods:**
  - `Optional<User> findByEmailIgnoreCase(String email)` — Find a user by email, case-insensitive.
  - `boolean existsByEmailIgnoreCase(String email)` — Check if an email is already registered.

### 2. `TransactionRepository`
- **Entity:** `Transaction`
- **Purpose:** Access and filter user and shared wallet transactions.
- **Custom Methods:**
  - `Page<Transaction> findByUserId(UUID userId, Pageable pageable)` — Paginated transactions for a user.
  - `Page<Transaction> findByUserIdAndEventDateBetween(UUID userId, LocalDate from, LocalDate to, Pageable pageable)` — Filter by date range.
  - `Page<Transaction> findBySharedWalletId(UUID sharedWalletId, Pageable pageable)` — Transactions for a shared wallet.
  - `long countByUserIdAndEventDateBetween(UUID userId, LocalDate from, LocalDate to)` — Count transactions in a period.

### 3. `BudgetRepository`
- **Entity:** `Budget`
- **Purpose:** Manage user budgets by month and category.
- **Custom Methods:**
  - `List<Budget> findByUserId(UUID userId)`
  - `List<Budget> findByUserIdAndMonth(UUID userId, String month)`
  - `Optional<Budget> findByUserIdAndMonthAndCategory(UUID userId, String month, String category)`
  - `boolean existsByUserIdAndMonthAndCategory(UUID userId, String month, String category)`

### 4. `NotificationRepository`
- **Entity:** `Notification`
- **Purpose:** Fetch and manage user notifications.
- **Custom Methods:**
  - `Page<Notification> findByUserId(UUID userId, Pageable pageable)`
  - `Page<Notification> findByUserIdAndReadFalse(UUID userId, Pageable pageable)` — Unread notifications.
  - `List<Notification> findTop20ByUserIdOrderByCreatedAtDesc(UUID userId)` — Recent notifications.

### 5. `RecurringJobRepository`
- **Entity:** `RecurringJob`
- **Purpose:** Track and schedule recurring transactions.
- **Custom Methods:**
  - `List<RecurringJob> findByActiveTrueAndNextRunAtBefore(Instant nextRunThreshold)` — Jobs due to run.
  - `List<RecurringJob> findByUserId(UUID userId)`
  - `Optional<RecurringJob> findByUserIdAndTemplateTransactionId(UUID userId, UUID transactionId)`

### 6. `SharedWalletRepository`
- **Entity:** `SharedWallet`
- **Purpose:** Manage shared wallets and ownership.
- **Custom Methods:**
  - `List<SharedWallet> findByOwnerId(UUID ownerId)`
  - `boolean existsByOwnerIdAndNameIgnoreCase(UUID ownerId, String name)`

### 7. `SharedWalletMemberRepository`
- **Entity:** `SharedWalletMember`
- **Purpose:** Manage wallet memberships and permissions.
- **Custom Methods:**
  - `List<SharedWalletMember> findByWalletId(UUID walletId)`
  - `List<SharedWalletMember> findByMemberId(UUID memberId)`
  - `Optional<SharedWalletMember> findByWalletIdAndMemberId(UUID walletId, UUID memberId)`

---

## Summary

Repositories in FinTrack provide a robust, type-safe, and maintainable way to interact with the database. They abstract away SQL and boilerplate, letting you focus on business logic in your services and controllers. Each repository is tailored to its entity, with custom queries that match the needs of your application.

For more details, see the JavaDoc in each repository interface or the main README.
