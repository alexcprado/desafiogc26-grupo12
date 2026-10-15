import "../styles/index.css"
import "../styles/stylesComponentes/Login.css"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUsers } from "@fortawesome/free-solid-svg-icons";
import { useState } from "react";

function Login() {
    const [ tipoUsuario, setTipoUsuario ] = useState<"admin" | "patrocinador">("admin");

    return (
    <main className="main-login">

        <div className="card-login">
            <div className="card-login-conteudo">

                <FontAwesomeIcon className="login-icon" icon={faUsers} />

                <div className="header-card">
                    <span className="texto-roxo texto-pequeno">BEM-VINDO AO PORTAL</span>
                    <h1>Acesse sua conta</h1>
                    <span className="texto-pequeno texto-cinza">Entre como equipe RBS ou usuário de patrocinador</span>
                </div>

                <div className="formulario-login">
                    <div className="botoes-formulario-login">
                        <button type="button" className={tipoUsuario === "admin" ? "active" : ""} onClick={() => setTipoUsuario("admin")}>Admin RBS</button>
                        <button type="button" className={tipoUsuario === "patrocinador" ? "active" : ""} onClick={() => setTipoUsuario("patrocinador")}>Patrocinador</button>
                    </div>

                    <div className="input-formulario-login texto-pequeno">
                        <label htmlFor="email">Email</label>
                        <input id="email" type="text" placeholder="exemplo@gmail.com" />

                        <label htmlFor="senha">Senha</label>
                        <input id="senha" type="password" placeholder="Digite sua senha"/>

                        <button type="submit">Enviar</button>

                        <p>Ainda não tem acesso administrativo? <a href="">Solicitar Cadastro</a></p>
                    </div>

                    <div className="footer-card-login texto-pequeno">
                        Seus dados são utilizados para gerenciar o acesso e a partcipação no evento. <br />
                        Não compartilhe sua senha.
                    </div>
                </div>

                <div>

                </div>
           
            </div>
        </div>
      
    </main>
    )

}

export default Login