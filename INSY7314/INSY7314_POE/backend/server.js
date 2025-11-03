import express from "express";
import dotenv from "dotenv";
import { connectDB } from "./config/db.js";
import Employee from "./models/employee.model.js";
import Customer from "./models/customer.model.js";
import Transaction from "./models/transaction.model.js";
import cors from 'cors';
import rateLimit from 'express-rate-limit';

dotenv.config();

const app = express();

//Highlights (2024) DDOS Protection:
const limiter = rateLimit({
  windowMs: 15 * 60 * 1000, // 15 minutes
  max: 100, // limit each IP to 100 requests per window
  message: {
    success: false,
    message: 'Too many requests from this IP, please try again later.'
  },
  standardHeaders: true, // Return rate limit info in the `RateLimit-*` headers
  legacyHeaders: false, // Disable the `X-RateLimit-*` headers
});

//Indusface (2025) Click jacking Protection:
app.use((req, res, next) => {
  res.setHeader("X-Frame-Options", "DENY");
  next();
});

app.use(cors({ origin: 'http://localhost:5173', credentials: true }));

app.use(express.json());// allows us to use to accept JSON data in req body freeCodeCamp.org (2024)

//Intialise server freeCodeCamp.org (2024)
app.get("/", (req,res) => {
    res.send("Server is ready");
});

//Employees:

//Add Employee: freeCodeCamp.org (2024) (remove/comment out after creating all employees)
app.post("/employees", async (req, res) => {
    const employee = req.body;

    if (!employee._id || !employee.employee_id || !employee.full_name || !employee.role || !employee.email || !employee.password) {
        return res.status(400).json({ success: false, message: "Please provide all fields" });
    }

    try{
        const newEmployee = new Employee(employee);
        await newEmployee.save();
        res.status(201).json({ success: true, data: newEmployee });
    } catch (error) {
        console.error("Error in creating employee:", error.message);
        res.status(500).json({ success: false, message: "Server Error" });
    }
});

//Search for Employee(Login): freeCodeCamp.org (2024)
app.post("/employees/login", async (req, res) => {
    const { employee_id, role, password } = req.body;

    // Validate input
    if (!employee_id || !role || !password) {
        return res.status(400).json({ success: false, message: "Please provide employee id, role, and password" });
    }

    try {
        // Find employee by full_name and account number
        const employee = await Employee.findOne({ employee_id, role });

        if (!employee) {
            return res.status(401).json({ success: false, message: "Incorrect Employee credentials." });
        }

        // Check password (plain text match – replace with password security with hashing and salting)
        if (employee.password !== password) {
            return res.status(401).json({ success: false, message: "Incorrect password" });
        }

        // Login successful
        res.status(200).json({
            success: true,
            message: "Login successful",
            data: {
                employee_id: employee.employee_id,
                full_name: employee.full_name,
                role: employee.role,
                email: employee.email
            }
        });
    } catch (error) {
        console.error("Error during customer login:", error.message);
        res.status(500).json({ success: false, message: "Server Error" });
    }
});


//Customers:

//Add Customer: freeCodeCamp.org (2024)
app.post("/customers", async (req, res) => {
    const customer = req.body;

    if (!customer.full_name || !customer.id_number || !customer.account_number || !customer.email || !customer.password) {
        return res.status(400).json({ success: false, message: "Please provide all fields" });
    }

    try{
        const newCustomer = new Customer(customer);
        await newCustomer.save();
        return res.status(201).json({ success: true, message: "Customer registered successfully", data: newCustomer });
    } catch (error) {
        console.error("Error in creating customer:", error.message);
        res.status(500).json({ success: false, message: "Server Error" });
    }
});


// Search for Customer (Login): freeCodeCamp.org (2024)
app.post("/customers/login", async (req, res) => {
    const { full_name, account_number, password } = req.body;

    // Validate input
    if (!full_name || !account_number || !password) {
        return res.status(400).json({ success: false, message: "Please provide full name, account number, and password" });
    }

    try {
        // Find customer by full_name and account number
        const customer = await Customer.findOne({ full_name, account_number });

        if (!customer) {
            return res.status(404).json({ success: false, message: "Customer not found. Please register first." });
        }

        const isMatch = await customer.comparePassword(password);

        if (!isMatch) {
            return res.status(401).json({ success: false, message: "Incorrect password" });
        }

        // Login successful
        res.status(200).json({
            success: true,
            message: "Login successful",
            data: {
                id: customer._id,
                full_name: customer.full_name,
                email: customer.email,
                account_number: customer.account_number
            }
        });
    } catch (error) {
        console.error("Error during customer login:", error.message);
        res.status(500).json({ success: false, message: "Server Error" });
    }
});


//Transactions:

//Add Transaction: freeCodeCamp.org (2024)
app.post("/transactions", async (req, res) => {
    const transaction = req.body;

    if (!transaction._id || !transaction.customer_id || !transaction.amount || !transaction.currency || !transaction.provider || !transaction.payee_account || !transaction.payee_swift || !transaction.status) {
        return res.status(400).json({ success: false, message: "Please provide all fields" });
    }

    try{
        const newTransaction = new Transaction(transaction);
        await newTransaction.save();
        res.status(201).json({ success: true, data: newTransaction });
    } catch (error) {
        console.error("Error in creating transaction:", error.message);
        res.status(500).json({ success: false, message: "Server Error" });
    }
});

//Get all pending Tansactions: freeCodeCamp.org (2024)
app.get("/transactions/fetch", async (req, res) => {
    try{
        const transactions = await Transaction.find({ status: "Pending" });
        res.status(200).json({ success: true, data: transactions });
    } catch (error) {
        console.error("Error in fetching transactions:", error);
        res.status(500).json({ success: false, message: "Server Error" });
    }
});

//Update Transaction (Status): freeCodeCamp.org (2024)
app.put("/transactions/update/:id", async (req, res) => {
    try {
        const { id } = req.params;
        const { status, verified_by, verified_at } = req.body;

        if (!id || !status || !verified_by || !verified_at) {
            return res.status(400).json({ 
                success: false, 
                message: "Please provide all required fields (status, verified_by, verified_at)" 
            });
        }

        // Update transaction by custom _id
        const updatedTransaction = await Transaction.findOneAndUpdate(
            { _id: id },
            { status, verified_by, verified_at },
            { new: true }
        );

        if (!updatedTransaction) {
            return res.status(404).json({ success: false, message: "Transaction not found" });
        }

        res.status(200).json({ success: true, data: updatedTransaction });
    } catch (error) {
        console.error("Error in updating transaction:", error);
        res.status(500).json({ success: false, message: "Server Error" });
    }
});

//freeCodeCamp.org (2024)
connectDB(app);


// app.listen(PORT
//     , () => {
//     connectDB();
//     console.log("Server started at http://localhost:" + PORT);
// });

/*
Reference list:
MERN Stack Tutorial with Deployment – Beginner's Course. 2024. YouTube video, added by freeCodeCamp.org. [Online]. Available at: https://www.youtube.com/watch?v=O3BUHwfHf84&t=1620s [Accessed 3 October 2025]. 
Highlights, F. 2024. Creating a Simple API Rate Limiter with NodeJs, Medium, 16 October 2024. [Blog]. Available at: https://medium.com/@ignatovich.dm/creating-a-simple-api-rate-limiter-with-node-a834d03bad7a [Accessed 3 October 2025]. 
Indusface. 2025. Understanding X-Frame-Options: Examples and Benefits, 3 September 2025. [Online]. Available at: https://www.indusface.com/learning/x-frame-options [Accessed 3 October 2025].
*/