# 🔐 UserAuth System - Full Stack Authentication
CLICK Registration System--> CLICK src --> CLICK main --> CLICK java -->registrationlogin --> SERVLET Registrationlogin.java FILE  | CLICK webapp ---> Registration.html -->Login.html--> Welcome

## 🎯 **What It Does**
- **Register** users with secure password validation
- **Login** users with database authentication
- **Welcome** authenticated users with personalized dashboard
- **Uses ALL 3 Servlet lifecycle methods** (init, service, destroy)


## 📁 **Project Structure**
```
UserAuthSystem/
├── 📝 register.html        (Sign up page)
├── 🔑 login.html           (Sign in page)
├── 🎉 welcome.jsp          (Success page)
├── ⚙️ UserAuthServlet.java (Brain of the system)
└── 📊 MySQL Database       (User storage)
```

## 🔄 **Servlet Lifecycle in Action**
```java
init() → 📞 "Hello Database!" (Connects to MySQL)
service() → 🧠 "Thinking..." (Handles register/login)
destroy() → 👋 "Bye Database!" (Closes connection)
```

## 🎨 **Features**
✅ **Modern UI** - Gradients, icons, smooth animations  
✅ **Form Validation** - Real-time password matching  
✅ **Database Security** - PreparedStatement prevents SQL injection  
✅ **Session Management** - Remembers logged-in users  
✅ **Error Handling** - User-friendly error messages  
✅ **Responsive Design** - Works on all devices  

## 🚦 **How It Works**
1. **User visits** → Sees beautiful login/register pages
2. **Clicks register** → Enters username + password
3. **Servlet processes** → Stores in MySQL database
4. **User logs in** → Credentials verified against DB
5. **Success** → Redirects to welcome dashboard 🎉

## 🎬 **Demo Flow**
```
1. Open browser → See login page
2. Click "Register" → Fill form
3. Submit → "Success!" message
4. Login → Redirect to welcome page
5. Logout → Back to start
```

