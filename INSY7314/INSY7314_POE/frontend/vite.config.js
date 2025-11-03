import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import fs from "fs";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: { //(SYD, 2025)
    https: { //(SYD, 2025)
      key: fs.readFileSync('ssl/key.pem'), //(SYD, 2025)
      cert: fs.readFileSync('ssl/cert.pem'), //(SYD, 2025)
    }
  }
})

/*
Reference list:

SYD, 2025. Vite https on localhost. [online] Available at: <https://www.bing.com/ck/a?!&&p=39192ddf05d187ed2b7630f4fdd24c1a506aed16cf21961c1623945fcf144880JmltdHM9MTc2MDA1NDQwMA&ptn=3&ver=2&hsh=4&fclid=3227c8fe-cdf0-6c19-0c38-dea0cc676dd2&u=a1aHR0cHM6Ly9zdGFja292ZXJmbG93LmNvbS9xdWVzdGlvbnMvNjk0MTc3ODgvdml0ZS1odHRwcy1vbi1sb2NhbGhvc3Q> [Accessed 10 October 2025]. 
*/