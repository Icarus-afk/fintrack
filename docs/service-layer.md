# FinTrack Service Layer – Technical Documentation

This document provides a comprehensive overview of the Service Layer architecture in the FinTrack backend, including design patterns, implementation strategies, and guidelines for extending the service layer.

---

## 1. Purpose and Architecture

The **Service Layer** sits between the **Controller** (API) and **Repository** (Data Access) layers, encapsulating all business logic, validation, and orchestration. It ensures:

- **Business Logic Encapsulation**: All domain rules, calculations, and workflows live in services
- **Transaction Management**: Declarative transaction boundaries using `@Transactional`
- **Security Integration**: Services enforce authorization rules and work with authentication context
- **DTO Mapping**: Services use mappers to convert between entities and DTOs
- **Cross-Cutting Concerns**: Logging, caching, and event publishing are handled here

---

## 2. Service Layer Structure

```
service/
  UserService.java             // Interface for user operations
  TransactionService.java      // Interface for transaction operations
  BudgetService.java           // Interface for budget operations
  NotificationService.java     // Interface for notification operations
  RecurringJobService.java     // Interface for recurring job operations
  SharedWalletService.java     // Interface for shared wallet operations
  
  impl/
    UserServiceImpl.java       // Implementation of UserService
    TransactionServiceImpl.java
    BudgetServiceImpl.java
    NotificationServiceImpl.java
    RecurringJobServiceImpl.java
    SharedWalletServiceImpl.java
```

---

## 3. Design Patterns and Principles

### 3.1 Interface-Based Design
- Each service has an interface (`UserService`) and implementation (`UserServiceImpl`)
- Controllers depend on interfaces, not implementations
- Enables testability (mock services in unit tests) and flexibility

### 3.2 Transaction Management
```java
@Service
@Transactional // All methods run in a transaction by default
public class UserServiceImpl implements UserService {
    
    @Transactional(readOnly = true) // Optimize read-only operations
    public UserProfileResponse getCurrentUserProfile(UUID userId) {
        // ...
    }
}
```

### 3.3 Dependency Injection
- Services are Spring beans (`@Service`)
- Dependencies injected via constructor (recommended) or field injection
- Example: `UserServiceImpl` injects `UserRepository`

### 3.4 DTO Mapping
- Services never expose entities in return types
- Use mappers to convert:  
  - **Request DTO → Entity** (for create/update)
  - **Entity → Response DTO** (for read operations)

### 3.5 Error Handling
- Services throw domain exceptions (e.g., `IllegalArgumentException`, `IllegalStateException`)
- Global exception handler in controller layer converts to HTTP responses
- Validate inputs early, fail fast

---

## 4. Service Interface Contracts

### 4.1 UserService
**Responsibilities:**
- User registration and authentication (login, logout, token refresh)
- Profile management (get, update, change password, avatar upload)
- Two-factor authentication (enable, disable, verify)
- Password reset (forgot password, reset with token)
- Admin operations (list users, delete user)

**Key Methods:**
- `register(RegisterRequest)`: Create new user, return tokens
- `login(LoginRequest)`: Authenticate, return tokens (or require 2FA)
- `getCurrentUserProfile(UUID)`: Get authenticated user's profile
- `updateProfile(UUID, UpdateProfileRequest)`: Update profile
- `enableTwoFactor(UUID)`: Enable 2FA, return QR code

**Design Notes:**
- Uses `UUID` for user IDs (entity layer standard)
- Password hashing delegated to `PasswordEncoder` (TODO)
- JWT token generation delegated to `JwtTokenProvider` (TODO)
- 2FA code generation/verification delegated to `TwoFactorAuthService` (TODO)
- Email notifications delegated to `EmailService` (TODO)
- Avatar uploads delegated to `FileStorageService` (TODO)

### 4.2 TransactionService
**Responsibilities:**
- CRUD operations on transactions
- Advanced filtering (date range, categories, amount range, type, shared wallet)
- Pagination and sorting
- Summary calculations (total income, expense, net balance)
- Shared wallet transaction access control

**Key Methods:**
- `createTransaction(UUID, CreateTransactionRequest)`: Create transaction, trigger budget check
- `getTransactions(UUID, TransactionFilter, Pageable)`: Filtered, paginated list
- `getTransactionSummary(UUID, TransactionFilter)`: Aggregate calculations
- `updateTransaction(UUID, Long, UpdateTransactionRequest)`: Update, re-trigger budget check
- `deleteTransaction(UUID, Long)`: Delete, re-check budgets

**Design Notes:**
- Access control: Users can only access their own transactions or shared wallet transactions they're members of
- After creating/updating/deleting a transaction, invoke `BudgetService.checkBudgetThresholds(userId)` to trigger alerts
- Use custom repository queries for filtering

### 4.3 BudgetService
**Responsibilities:**
- CRUD operations on budgets
- Budget utilization tracking (used amount vs. total amount)
- Threshold alerts (notify when budget exceeds configured percentage)
- Monthly budget queries

**Key Methods:**
- `createBudget(UUID, CreateBudgetRequest)`: Create budget
- `getBudgetsByMonth(UUID, YearMonth, Pageable)`: Get budgets for a specific month
- `checkBudgetThresholds(UUID)`: Calculate utilization for all user budgets, send notifications if threshold exceeded

**Design Notes:**
- `usedAmount` is calculated by summing transactions in the budget's category and month
- `BudgetMapper` computes derived fields (`remainingAmount`, `percentageUsed`)
- `checkBudgetThresholds()` is invoked by `TransactionService` after mutations

### 4.4 NotificationService
**Responsibilities:**
- Create notifications for users (budget alerts, shared wallet invites, etc.)
- Retrieve notifications (all, unread, by ID)
- Mark as read (single, bulk, all)
- Delete notifications

**Key Methods:**
- `createNotification(UUID, NotificationType, String, String, Map<String, String>)`: Create notification with metadata
- `getUnreadNotifications(UUID, Pageable)`: Paginated unread list
- `markNotificationsAsRead(UUID, MarkNotificationsReadRequest)`: Bulk mark as read
- `markAllNotificationsAsRead(UUID)`: Mark all as read

**Design Notes:**
- Notifications are fire-and-forget (no guaranteed delivery)
- For real-time push, integrate with WebSocket or Server-Sent Events
- Metadata stores context (e.g., budget ID, transaction ID)

### 4.5 RecurringJobService
**Responsibilities:**
- Schedule recurring transactions (daily, weekly, monthly, etc.)
- Execute due recurring jobs (scheduler invokes this)
- CRUD operations on recurring jobs
- Active/inactive state management

**Key Methods:**
- `createRecurringJob(UUID, CreateRecurringJobRequest)`: Schedule new recurring job
- `executeRecurringJobs()`: Find all due jobs, create transactions, update `nextRunAt` and `lastRunAt`
- `getActiveRecurringJobs(UUID, Pageable)`: List active jobs

**Design Notes:**
- `executeRecurringJobs()` is invoked by a `@Scheduled` task (e.g., every hour)
- Each execution creates a `Transaction` from the template and updates the job's `nextRunAt` based on frequency
- Use custom query: `findByActiveTrueAndNextRunAtBeforeOrderByNextRunAtAsc(LocalDateTime.now())`

### 4.6 SharedWalletService
**Responsibilities:**
- CRUD operations on shared wallets
- Member management (add, update, remove)
- Access control (owner, admin, member roles)
- Balance calculations per member (based on share ratio)

**Key Methods:**
- `createSharedWallet(UUID, CreateSharedWalletRequest)`: Create wallet, user becomes owner
- `addMember(UUID, Long, AddSharedWalletMemberRequest)`: Add member to wallet
- `updateMember(UUID, Long, Long, UpdateSharedWalletMemberRequest)`: Update member details
- `removeMember(UUID, Long, Long)`: Remove member

**Design Notes:**
- Access control: Owner can do all operations, admins can add/remove members, members can view
- `runningBalance` is calculated by summing transactions in the shared wallet multiplied by the member's share ratio
- Use repository methods: `findByIdAndOwnerId`, `findBySharedWalletIdAndMemberId`

---

## 5. Implementation Guidelines

### 5.1 Creating a New Service

1. **Define the interface** in `service/` package
2. **Create the implementation** in `service/impl/` package
3. **Annotate implementation** with `@Service` and `@Transactional`
4. **Inject repositories** and other services via constructor
5. **Use mappers** for DTO conversions
6. **Handle errors** with meaningful exceptions
7. **Add JavaDoc** to interface methods

Example:

```java
// Interface
public interface ExampleService {
    ExampleResponse getExample(UUID id);
}

// Implementation
@Service
@Transactional
public class ExampleServiceImpl implements ExampleService {
    private final ExampleRepository repository;
    
    public ExampleServiceImpl(ExampleRepository repository) {
        this.repository = repository;
    }
    
    @Override
    @Transactional(readOnly = true)
    public ExampleResponse getExample(UUID id) {
        Example entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Example not found"));
        return ExampleMapper.toResponse(entity);
    }
}
```

### 5.2 Access Control Patterns

**Pattern 1: User-specific resources**
```java
public BudgetResponse getBudgetById(UUID userId, Long budgetId) {
    Budget budget = budgetRepository.findByIdAndUserId(budgetId, userId)
        .orElseThrow(() -> new IllegalArgumentException("Budget not found or access denied"));
    return BudgetMapper.toResponse(budget);
}
```

**Pattern 2: Shared wallet resources (owner/member check)**
```java
public SharedWalletResponse getSharedWalletById(UUID userId, Long walletId) {
    // User must be owner or member
    SharedWallet wallet = sharedWalletRepository.findById(walletId)
        .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));
    
    if (!wallet.getOwnerId().equals(userId) && 
        !sharedWalletMemberRepository.existsBySharedWalletIdAndMemberId(walletId, userId)) {
        throw new SecurityException("Access denied");
    }
    
    return SharedWalletMapper.toResponse(wallet);
}
```

### 5.3 Transaction Boundaries

- Use `@Transactional` at class level for write-heavy services
- Use `@Transactional(readOnly = true)` for read-only methods (optimization)
- Avoid long-running transactions (network calls, file I/O inside transactions)

### 5.4 Validation Strategy

- Validate at DTO level using Jakarta Bean Validation (`@NotNull`, `@Email`, etc.) — controllers enforce this
- Validate business rules in service layer (e.g., "budget amount must be positive")
- Throw `IllegalArgumentException` or `IllegalStateException` for validation failures

---

## 6. Integration Points

### 6.1 Security Integration (TODO)
- Services receive `UUID userId` from authenticated principal (extracted in controller from JWT)
- Use `SecurityContextHolder.getContext().getAuthentication()` to get current user in controller
- Pass `userId` to service methods explicitly

### 6.2 Email Service (TODO)
- `UserService` should inject `EmailService` for password reset, welcome emails
- `NotificationService` could optionally send email notifications

### 6.3 File Storage Service (TODO)
- `UserService.uploadAvatar()` should delegate to `FileStorageService` (S3, local filesystem)
- Store file path/URL in `User.avatarUrl`

### 6.4 JWT Token Provider (TODO)
- `UserService` should inject `JwtTokenProvider` to generate/refresh tokens
- `JwtTokenProvider.generateToken(User)` returns access and refresh tokens

### 6.5 Two-Factor Auth Service (TODO)
- `UserService` should inject `TwoFactorAuthService` to generate TOTP secrets and verify codes
- Use a library like `GoogleAuthenticator` or `TOTP`

### 6.6 Scheduler Integration
- `RecurringJobService.executeRecurringJobs()` is invoked by a `@Scheduled` method in a separate component
- Example:
```java
@Component
public class RecurringJobScheduler {
    private final RecurringJobService recurringJobService;
    
    public RecurringJobScheduler(RecurringJobService recurringJobService) {
        this.recurringJobService = recurringJobService;
    }
    
    @Scheduled(cron = "0 0 * * * *") // Every hour
    public void executeRecurringJobs() {
        recurringJobService.executeRecurringJobs();
    }
}
```

---

## 7. Testing Strategy

### 7.1 Unit Tests
- Mock repositories and other dependencies
- Test business logic in isolation
- Example:
```java
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserServiceImpl userService;
    
    @Test
    void getCurrentUserProfile_shouldReturnProfile() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        
        UserProfileResponse response = userService.getCurrentUserProfile(userId);
        
        assertNotNull(response);
        assertEquals("test@example.com", response.email());
    }
}
```

### 7.2 Integration Tests
- Use `@SpringBootTest` and `@Transactional` (auto-rollback)
- Test with real repositories and database
- Example:
```java
@SpringBootTest
@Transactional
class UserServiceIntegrationTest {
    @Autowired
    private UserService userService;
    
    @Test
    void register_shouldCreateUserAndReturnTokens() {
        RegisterRequest request = new RegisterRequest("John Doe", "john@example.com", "password123");
        TokenResponse response = userService.register(request);
        
        assertNotNull(response);
        assertNotNull(response.accessToken());
    }
}
```

---

## 8. Current Implementation Status

### Completed:
- **UserService interface and implementation** (with TODOs for password encoding, JWT, 2FA, email, file storage)
- **Service interfaces** for Transaction, Budget, Notification, RecurringJob, SharedWallet
- **Dependencies added** to `pom.xml`: `spring-boot-starter-web`, `spring-boot-starter-validation`, `spring-boot-starter-security`, JWT libraries, PostgreSQL driver

### TODO (Next Steps):
1. **Implement remaining service implementations** (Transaction, Budget, Notification, RecurringJob, SharedWallet) following the `UserServiceImpl` pattern
2. **Add validation annotations** to all request DTOs (`@NotNull`, `@Email`, `@Size`, `@Min`, `@Max`, etc.)
3. **Implement security infrastructure**:
   - `PasswordEncoder` bean (BCrypt)
   - `JwtTokenProvider` for token generation/validation
   - Spring Security configuration (JWT filter, authentication manager)
4. **Implement auxiliary services**:
   - `EmailService` for sending emails (password reset, notifications)
   - `FileStorageService` for avatar uploads
   - `TwoFactorAuthService` for TOTP generation/verification
5. **Add scheduler** for `RecurringJobService.executeRecurringJobs()`
6. **Write unit and integration tests** for all services
7. **Add global exception handler** in controller layer
8. **Document controllers** (next layer)

---

## 9. Best Practices

- **Keep services thin**: Delegate complex logic to helper classes or domain models
- **Avoid circular dependencies**: If Service A needs Service B and vice versa, refactor or use events
- **Use events for decoupling**: Instead of `TransactionService` directly calling `BudgetService`, publish a `TransactionCreatedEvent` and have `BudgetService` listen
- **Cache expensive operations**: Use `@Cacheable` for frequently accessed, rarely changing data
- **Log important actions**: Use SLF4J to log registrations, logins, errors
- **Version APIs**: If API contract changes, consider versioning (`/api/v1`, `/api/v2`)

---

## 10. Summary

The Service Layer in FinTrack is the **heart of the application**, orchestrating business logic, transaction management, and integration with external services. It:
- Encapsulates all domain logic
- Enforces security and access control
- Uses DTOs exclusively for API contracts
- Supports extensibility and testability

For further implementation, follow the patterns established in `UserServiceImpl` and adapt to each domain area (Transaction, Budget, etc.).

**Next Step**: Implement controllers to expose services as REST APIs, or complete the remaining service implementations and add validation annotations to request DTOs.
