import "../styles/index.css"
import "../styles/stylesComponentes/Login.css"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUsers } from "@fortawesome/free-solid-svg-icons";
import { useState } from "react";

function Login() {
    const [ tipoUsuario, setTipoUsuario ] = useState<"admin" | "patrocinador">("admin");
    const [cnpj, setCnpj] = useState("");

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
                        <input id="email" name="email" type="text" placeholder="exemplo@gmail.com" required/>

                        {tipoUsuario === "patrocinador" && (
                            <div className="cnpj-input-login-patrocinador">
                                <label htmlFor="cnpj">CNPJ</label>
                                <input type="text" id="cnpj" name="cnpj" value={cnpj} placeholder="00.000.000/0000-00" inputMode="numeric" required onChange={(e) => {const valor = e.target.value.replace(/\D/g, ""); setCnpj(valor.slice(0, 14))}}/>
                            </div>
                        )}

                        <label htmlFor="senha">Senha</label>
                        <input id="senha" name="senha" type="password" placeholder="Digite sua senha" required/>

                        <button type="submit">Enviar</button>

                        <p>Ainda não tem acesso? <a href="">Solicitar Cadastro</a></p>
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