export const regexPatterns = {
  full_name: /^[A-Za-z\s]{2,50}$/,
  id_number: /^\d{13}$/,
  account_number: /^\d{8,20}$/,
  email: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
  password: /^[A-Za-z0-9!@#\$%\^&\*]{6,30}$/,
  employee_id: /^[A-Z0-9]{4,10}$/,
  role: /^[A-Za-z\s]{3,20}$/,
  amount: /^\d+(\.\d{1,2})?$/,
  currency: /^[A-Z]{3}$/,
  payee_account: /^\d{8,20}$/,
  payee_swift: /^[A-Z0-9]{8,11}$/
};

export const validateField = (name, value) => {
  const pattern = regexPatterns[name];
  if (!pattern) return true; // ignore if not defined
  return pattern.test(value);
};