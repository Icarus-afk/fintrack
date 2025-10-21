# FinTrack Backend: Entity Layer Technical Overview

This document provides a detailed technical explanation of the `entity` folder in the FinTrack backend. It describes the purpose, structure, and relationships of each entity, as well as the architectural patterns and best practices applied.

---

## Purpose of the `entity` Folder

The `entity` folder contains all Java classes that represent the core domain model of the FinTrack application. These classes are mapped to database tables using JPA (Jakarta Persistence API) annotations and define the structure, constraints, and relationships of the application's persistent data.

Entities are the foundation of the backend, enabling object-relational mapping (ORM) between Java objects and relational database tables. They are used throughout the repository, service, and controller layers.

---

## Folder Structure

```
entity/
  Budget.java
  Notification.java
  RecurringJob.java
  SharedWallet.java
  SharedWalletMember.java
  Transaction.java
  User.java
  base/
    AuditableEntity.java
  enums/
    NotificationType.java
    RecurringFrequency.java
    TransactionType.java
    UserRole.java
```

---

## Base Class

### `base/AuditableEntity.java`
- **Purpose:** Provides common audit fields (`createdAt`, `updatedAt`) for all entities.
- **Usage:** All entities extend this class to automatically inherit audit columns, ensuring consistent tracking of creation and modification timestamps.
- **Annotations:** Uses JPA's `@MappedSuperclass` and `@EntityListeners` for automatic population.

---

## Enums

Located in `entity/enums/`, these enums provide type safety and restrict values for certain fields:
- **UserRole:** Defines user roles (e.g., USER, ADMIN).
- **TransactionType:** Categorizes transactions (INCOME, EXPENSE, TRANSFER, etc.).
- **NotificationType:** Types of notifications (e.g., BUDGET_EXCEEDED, INVITE).
- **RecurringFrequency:** Frequency for recurring jobs (DAILY, WEEKLY, MONTHLY, YEARLY).

---

## Core Entities

### `User.java`
- **Represents:** Application user (account holder).
- **Key Fields:**
  - `id` (UUID): Primary key.
  - `email`: Unique, required.
  - `passwordHash`: BCrypt-hashed password.
  - `fullName`, `avatarUrl`, `twoFactorSecret`.
  - `role`: Enum (UserRole).
- **Relationships:**
  - One-to-many with `Transaction`, `Budget`, `Notification`, `RecurringJob`.
  - One-to-many with `SharedWalletMember` (as member of shared wallets).
- **Notes:**
  - Enforces unique email via DB index.
  - Supports 2FA and role-based access.

### `Transaction.java`
- **Represents:** Financial transaction (income, expense, transfer).
- **Key Fields:**
  - `id` (UUID), `amount`, `description`, `date`, `type` (TransactionType), `category`.
- **Relationships:**
  - Many-to-one with `User` (owner).
  - Many-to-one with `SharedWallet` (if part of a shared wallet).
- **Notes:**
  - Supports categorization and notes.

### `Budget.java`
- **Represents:** User-defined budget for a category and period.
- **Key Fields:**
  - `id` (UUID), `category`, `amount`, `periodStart`, `periodEnd`.
- **Relationships:**
  - Many-to-one with `User` (owner).
- **Notes:**
  - Used to track and limit spending.

### `Notification.java`
- **Represents:** System notification for a user.
- **Key Fields:**
  - `id` (UUID), `type` (NotificationType), `message`, `read` (boolean), `createdAt`.
- **Relationships:**
  - Many-to-one with `User` (recipient).
- **Notes:**
  - Used for alerts (budget exceeded, invites, etc.).

### `RecurringJob.java`
- **Represents:** Scheduled job to create recurring transactions.
- **Key Fields:**
  - `id` (UUID), `frequency` (RecurringFrequency), `nextRun`, `active` (boolean).
- **Relationships:**
  - Many-to-one with `User` (owner).
  - Many-to-one with `Transaction` (template for recurrence).
- **Notes:**
  - Automates periodic transactions (e.g., salary, rent).

### `SharedWallet.java`
- **Represents:** Collaborative wallet for multiple users.
- **Key Fields:**
  - `id` (UUID), `name`, `description`, `createdAt`.
- **Relationships:**
  - Many-to-one with `User` (owner).
  - One-to-many with `SharedWalletMember` (members).
  - One-to-many with `Transaction` (shared transactions).
- **Notes:**
  - Enables group expense tracking and collaboration.

### `SharedWalletMember.java`
- **Represents:** Membership of a user in a shared wallet.
- **Key Fields:**
  - `id` (UUID), `role` (e.g., MEMBER, ADMIN).
- **Relationships:**
  - Many-to-one with `User` (member).
  - Many-to-one with `SharedWallet`.
- **Notes:**
  - Manages permissions and invitations for shared wallets.

---

## Relationships & Constraints

- **UUIDs** are used as primary keys for all entities, ensuring global uniqueness.
- **JPA Annotations** define table mappings, indexes, and relationships (e.g., `@OneToMany`, `@ManyToOne`).
- **Cascade Types** and `orphanRemoval` are set to manage child entity lifecycles.
- **Validation** is enforced via field constraints (e.g., `nullable = false`, `length`).
- **Indexes** (e.g., unique email) improve query performance and enforce uniqueness.

---

## Best Practices Applied

- **Separation of Concerns:** Entities are pure data models, free of business logic.
- **Extensibility:** New fields or relationships can be added with minimal impact.
- **Auditability:** All entities track creation and update times.
- **Type Safety:** Enums restrict possible values for roles, types, and frequencies.
- **Security:** Sensitive fields (passwords, 2FA secrets) are protected and never exposed directly.

---

## Summary

The `entity` folder is the backbone of the FinTrack backend, defining the persistent data model and relationships. It leverages JPA for ORM, enforces data integrity, and provides a robust foundation for the repository, service, and controller layers that implement the application's business logic and API.

For further details, see the Javadoc comments in each entity class or the main design documentation.
