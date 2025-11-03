import mongoose from "mongoose";
import bcrypt from "bcrypt";

//Model.js freeCodeCamp.org (2024)

const customerSchema = new mongoose.Schema({
    id_number: {
        type: String,
        required: true
    },
    full_name: {
        type: String,
        required: true
    },
    account_number: {
        type: String,
        required: true
    },
    email: {
        type: String,
        required: true
    },
    password: {
        type: String,
        required: true
    },
}, {
    timestamps: true // createdAt + updatedAt
});

// Pre-save middleware to hash password (bcrypt.js, 2024; Mongoose, 2025)
customerSchema.pre("save", async function(next) {
    if (!this.isModified("password")) {
        return next(); // skip if password hasn't changed
    }

    try {
        const saltRounds = 10;
        const salt = await bcrypt.genSalt(saltRounds);
        this.password = await bcrypt.hash(this.password, salt);
        next();
    } catch (error) {
        next(error);
    }
});

// Method to compare password later  (bcrypt.js, 2024; Mongoose, 2025)
customerSchema.methods.comparePassword = async function(candidatePassword) {
    return await bcrypt.compare(candidatePassword, this.password);
};

const Customer = mongoose.model("Customer", customerSchema);

export default Customer;

/*
Reference list:
MERN Stack Tutorial with Deployment – Beginner's Course. 2024. YouTube video, added by freeCodeCamp.org. [Online]. Available at: https://www.youtube.com/watch?v=O3BUHwfHf84&t=1620s [Accessed 3 October 2025]. 

bcrypt.js, 2024. bcrypt.js GitHub Repository. [online] Available at: https://github.com/kelektiv/node.bcrypt.js
[Accessed 10 October 2025].

Mongoose, 2025. Middleware (Hooks). [online] Mongoose ODM. Available at: https://mongoosejs.com/docs/middleware.html
[Accessed 10 October 2025].
*/