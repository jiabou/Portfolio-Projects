import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { validateField } from "../utils/regexValidation.js";

//Dave Gray (2022) Register page:
const CustomerRegister = () => {
  const [formData, setFormData] = useState({
    full_name: "",
    id_number: "",
    account_number: "",
    email: "",
    password: "",
  });
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");
    setError("");

    for (const [key, value] of Object.entries(formData)) {
      if (!validateField(key, value)) {
        return setError(`Invalid ${key.replace("_", " ")}`);
      }
    }

    try {
      const res = await axios.post("http://localhost:5000/customers", formData);
      if (res.data.success) {
        setMessage("Registration successful! Redirecting...");
        setTimeout(() => navigate("/customer/login"), 1500);
      } else {
        setError(res.data.message || "Registration failed");
      }
    } catch {
      setError("Server error. Try again later.");
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Customer Registration</h2>
      {message && <p style={{ color: "green" }}>{message}</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}
      <input name="full_name" placeholder="Full Name" onChange={handleChange} required />
      <input name="id_number" placeholder="ID Number" onChange={handleChange} required />
      <input name="account_number" placeholder="Account Number" onChange={handleChange} required />
      <input name="email" placeholder="Email" type="email" onChange={handleChange} required />
      <input name="password" placeholder="Password" type="password" onChange={handleChange} required />
      <button type="submit">Register</button>
    </form>
  );
};

export default CustomerRegister;

/*
Reference list:
React.js App Project | MERN Stack Tutorial. 2022. YouTube video, added by Dave Gray. [Online]. Available at: https://www.youtube.com/watch?v=5cc09qZK0VU [Accessed 9 October 2025]. 
*/