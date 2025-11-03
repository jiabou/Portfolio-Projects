import mongoose from "mongoose";

//Model.js freeCodeCamp.org (2024)

const employeeSchema = new mongoose.Schema({
    _id:{
        type: String,
        required: true
    },
    employee_id:{
        type: String,
        required: true
    },
    full_name:{
        type: String,
        required: true
    },
    role:{
        type: String,
        required: true
    },
    email:{
        type: String,
        required: true
    },
    password:{
        type: String,
        required: true
    },
}, {
    timestamps: true //createdAt + updatedAt freeCodeCamp.org (2024)
});

const Employee = mongoose.model("Employee", employeeSchema);

export default Employee;

/*
Reference list:
MERN Stack Tutorial with Deployment – Beginner's Course. 2024. YouTube video, added by freeCodeCamp.org. [Online]. Available at: https://www.youtube.com/watch?v=O3BUHwfHf84&t=1620s [Accessed 3 October 2025]. 
*/