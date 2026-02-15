# Verify Data in H2 Database

Your backend and frontend are now connected! Here's how to verify that user data is being stored:

## ✅ Frontend & Backend Status

- **Backend**: Running on `http://localhost:8080/api` ✓
- **Frontend**: Running on `http://localhost:5173` ✓
- **Database**: H2 (in-memory) at `jdbc:h2:mem:testdb` ✓

---

## 📝 Steps to Test Registration & See Data

### 1. **Register a New User**

1. Go to `http://localhost:5173/register`
2. Fill in the form:
   - **Name**: `John Smith`
   - **Email**: `john@example.com`
   - **Password**: `password123`
3. Click **Register**
4. Should see success message → redirects to login

### 2. **View Data in H2 Console**

1. Open browser: `http://localhost:8080/h2-console`
2. **Connection Settings** (should be auto-filled):
   - **JDBC URL**: `jdbc:h2:mem:testdb`
   - **User Name**: `sa`
   - **Password**: (leave blank)
3. Click **Connect**

### 3. **View All Users**

In the H2 console, run this SQL query:

```sql
SELECT * FROM users;
```

You'll see your registered users with:
- `user_id` (auto-generated)
- `username` (from email prefix)
- `email`
- `password_hash` (encrypted)
- `first_name`
- `last_name`
- `role` (USER by default)
- `is_active` (true by default)
- `created_at` (timestamp)
- `updated_at`
- `last_login` (null until they login)

### 4. **Test Login**

1. Go to `http://localhost:5173/login`
2. Enter credentials:
   - **Email**: `john@example.com`
   - **Password**: `password123`
3. Click **Sign in** → Should go to Dashboard

### 5. **Check Last Login Updated**

Run again in H2 console:

```sql
SELECT user_id, username, email, last_login FROM users;
```

The `last_login` field should now show the timestamp of when you logged in.

---

## 🔍 Useful H2 SQL Queries

### View all users
```sql
SELECT * FROM users;
```

### View specific user by email
```sql
SELECT * FROM users WHERE email = 'john@example.com';
```

### Count total users
```sql
SELECT COUNT(*) as total_users FROM users;
```

### View user details
```sql
SELECT user_id, username, email, first_name, last_name, is_active, created_at FROM users;
```

### Check password hash (encrypted)
```sql
SELECT username, password_hash FROM users;
```

### Users created today
```sql
SELECT * FROM users WHERE DATE(created_at) = CURDATE();
```

---

## 🐛 Troubleshooting

### **Issue**: "Failed to connect to server" when registering

**Solution**: 
- Make sure backend is still running: `http://localhost:8080/api/auth/login` should be accessible
- Check that Spring Boot application hasn't stopped
- Restart backend if needed: `mvn spring-boot:run`

### **Issue**: H2 Console says "No data found"

**Solution**:
- Make sure you see the `users` table in the left panel
- If not, click refresh or reload the page
- Tables are created automatically on first request

### **Issue**: Can't login after registering

**Solution**:
- Use the exact email you registered with
- Password is case-sensitive
- Check browser console for error messages (F12 → Console tab)

### **Issue**: "Connection refused" when opening H2 console

**Solution**:
- Backend must be running: `http://localhost:8080/h2-console`
- Don't close the backend terminal

---

## 🔒 Password Security Note

Passwords are stored as **BCrypt hashes**, not plaintext. In H2 console you'll see something like:

```
$2a$10$4R9if.5qNRG8D9vD8k7R.eFLK7.8K2kE7kL...
```

This is normal! The system automatically hashes passwords when you register.

---

## 📊 Database Schema

**Users Table Structure**:

```
Column Name      | Type              | Description
─────────────────┼───────────────────┼──────────────────────────
user_id          | INTEGER PK        | Auto-increment ID
username         | VARCHAR(255)      | Unique username
email            | VARCHAR(255)      | Unique email
password_hash    | VARCHAR(255)      | BCrypt encrypted password
first_name       | VARCHAR(255)      | User's first name
last_name        | VARCHAR(255)      | User's last name
role             | ENUM              | ADMIN, USER, or GUEST
is_active        | BOOLEAN           | Account active status
created_at       | TIMESTAMP         | Registration timestamp
updated_at       | TIMESTAMP         | Last update timestamp
last_login       | TIMESTAMP         | Last login timestamp
```

---

## 🎯 Complete Test Workflow

```
1. Frontend Registration Page
   ↓ (fills form)
   ↓
2. Backend /auth/register endpoint
   ↓ (validates, hashes password, saves)
   ↓
3. H2 Database stores user
   ↓
4. Frontend Login Page
   ↓ (enters credentials)
   ↓
5. Backend /auth/login endpoint
   ↓ (verifies, updates last_login, generates token)
   ↓
6. H2 Database updates last_login timestamp
   ↓
7. Frontend Dashboard (authenticated)
```

---

## 📱 Live Testing Right Now

1. **Open 3 browser tabs/windows**:
   - Tab 1: `http://localhost:5173/register` (Frontend)
   - Tab 2: `http://localhost:8080/h2-console` (Database)
   - Tab 3: `http://localhost:5173/login` (Frontend)

2. **Register a user in Tab 1**

3. **Refresh Tab 2 and query**: `SELECT * FROM users;`

4. **See the new user data in the database!**

5. **Login in Tab 3 with the same credentials**

6. **Query in Tab 2 again** to see `last_login` updated

---

**✨ You now have a full-stack auth system with data persistence!**
