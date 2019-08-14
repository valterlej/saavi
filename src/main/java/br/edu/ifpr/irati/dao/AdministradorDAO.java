/*
 * Copyright 2019 Valter Luís Estevam Junior
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 * 
 */
package br.edu.ifpr.irati.dao;

import br.edu.ifpr.irati.exception.LoginException;
import br.edu.ifpr.irati.modelo.Administrador;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * Classe para acesso aos dados de um administrador.
 * 
 * @author Valter Estevam
 */
public class AdministradorDAO {

    /**
     * Buscar um usuário administrador pelo e-mail e senha dados por parâmetro.
     * 
     * @param email e-mail cadastrado no sistema.
     * @param senha senha vinculada com a conta.
     * @return Administrador logado no sistema.
     * @throws LoginException Acesso recusado.
     */
    public Administrador buscarPorEmailSenha(String email, String senha) throws LoginException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        String hql = "from administrador as adm where adm.email ='" + email + "' and adm.senha='" + senha + "'";
        Query query = session.createQuery(hql);
        List results = query.list();
        session.getTransaction().commit();
        session.close();
        if (!results.isEmpty()) {
            return (Administrador) results.get(0);
        } else {
            throw new LoginException("Usuário ou senha inválido!");
        }
    }

}