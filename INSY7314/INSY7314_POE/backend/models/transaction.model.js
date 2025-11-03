import mongoose from "mongoose";

//Model.js freeCodeCamp.org (2024)

const transactionSchema = new mongoose.Schema({
    _id:{
        type: String,
        required: true
    },
    customer_id:{
        type: String,
        required: true
    },
    amount:{
        type: Number,
        required: true
    },
    currency:{
        type: String,
        required: true
    },
    provider:{
        type: String,
        required: true
    },
    payee_account:{
        type: String,
        required: true
    },
    payee_swift:{
        type: String,
        required: true
    },
    status:{
        type: String,
        required: true
    },
    verified_by: {
        type: String,
        required: false,
        default: null
    },
    verified_at: {
        type: String,
        required: false,
        default: null
    },
}, {
    timestamps: true //createdAt + updatedAt freeCodeCamp.org (2024)
});

const Transaction = mongoose.model("Transaction", transactionSchema);

export default Transaction;

/*
Reference list:
MERN Stack Tutorial with Deployment – Beginner's Course. 2024. YouTube video, added by freeCodeCamp.org. [Online]. Available at: https://www.youtube.com/watch?v=O3BUHwfHf84&t=1620s [Accessed 3 October 2025]. 
*/