import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { validateField } from "../utils/regexValidation.js";

//Dave Gray (2022) Login page:
const CustomerLogin = () => {
  const [form, setForm] = useState({ full_name: "", account_number: "", password: "" });
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    for (const [key, value] of Object.entries(form)) {
      if (!validateField(key, value)) {
        return setError(`Invalid ${key.replace("_", " ")}`);
      }
    }

    try {
      const res = await axios.post("http://localhost:5000/customers/login", form);
      if (res.data.success) {
        navigate("/make-payments");
      } else {
        setError(res.data.message || "Login failed");
      }
    } catch {
      setError("Server error. Try again later.");
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Customer Login</h2>
      {error && <p style={{ color: "red" }}>{error}</p>}
      <input name="full_name" placeholder="Full Name" onChange={handleChange} required />
      <input name="account_number" placeholder="Account Number" onChange={handleChange} required />
      <input name="password" type="password" onChange={handleChange} required />
      <button type="submit">Login</button>
    </form>
  );
};

export default CustomerLogin;

/*
Reference list:
React.js App Project | MERN Stack Tutorial. 2022. YouTube video, added by Dave Gray. [Online]. Available at: https://www.youtube.com/watch?v=5cc09qZK0VU [Accessed 9 October 2025]. 
*/