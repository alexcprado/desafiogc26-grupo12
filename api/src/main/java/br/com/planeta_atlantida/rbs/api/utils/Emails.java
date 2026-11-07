package br.com.planeta_atlantida.rbs.api.utils;

import java.util.Locale;

public final class Emails {

    private Emails() {
    }

    /**
     * Normaliza o e-mail para comparação e armazenamento: sem espaços nas pontas
     * e em minúsculas. O Postgres compara e-mails com distinção de maiúsculas, então
     * "Admin@planeta.dev" não encontrava "admin@planeta.dev".
     */
    public static String normalizar(String email) {
        if (email == null) {
            return null;
        }
        return email.trim().toLowerCase(Locale.ROOT);
    }

}
