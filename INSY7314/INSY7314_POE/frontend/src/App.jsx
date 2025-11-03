import React from "react";
import { Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar.jsx";
import CustomerRegister from "./pages/CustomerRegister.jsx";
import CustomerLogin from "./pages/CustomerLogin.jsx";
import MakePayments from "./pages/MakePayments.jsx";
import EmployeeLogin from "./pages/EmployeeLogin.jsx";
import InternationalPaymentsPortal from "./pages/InternationalPaymentsPortal.jsx";

//Dave Gray (2022) main page:
function App() {
  return (
    <>
      <Navbar />
      <Routes>
        <Route path="/register" element={<CustomerRegister />} />
        <Route path="/customer/login" element={<CustomerLogin />} />
        <Route path="/employee/login" element={<EmployeeLogin />} />
        <Route path="/make-payments" element={<MakePayments />} />
        <Route path="/portal" element={<InternationalPaymentsPortal />} />
        <Route path="*" element={<div>404 Not Found</div>} />
      </Routes>
    </>
  );
}

export default App;
/*
Reference list:
React.js App Project | MERN Stack Tutorial. 2022. YouTube video, added by Dave Gray. [Online]. Available at: https://www.youtube.com/watch?v=5cc09qZK0VU [Accessed 9 October 2025]. 
*/