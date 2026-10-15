import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './styles/index.css'
// import App from './App.tsx'
import Login from './componentes/Login.tsx'
// import { BrowserRouter } from 'react-router-dom';

createRoot(document.getElementById('root')!).render(

  <StrictMode>

      <Login />


  </StrictMode>,
)
