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
import br.edu.ifpr.irati.exception.PersistenceException;
import br.edu.ifpr.irati.modelo.Campus;
import br.edu.ifpr.irati.modelo.Entrevistado;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * Classe para acesso aos dados de um entrevistado.
 * 
 * @author Valter Estevam
 */
public class EntrevistadoDAO {

    /**
     * 
     * Buscar um entrevistado pelo CPF associado.
     * 
     * @param cpf número do cadastro de pessoa física.
     * @return Entrevistado localizado.
     * @throws PersistenceException 
     */
    public Entrevistado buscarPorCPF(String cpf) throws PersistenceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        String hql = "from entrevistado as ent where ent.cpf='" + cpf + "'";
        Query query = session.createQuery(hql);
        List results = query.list();
        session.getTransaction().commit();
        session.close();
        if (!results.isEmpty()) {
            return (Entrevistado) results.get(0);
        } else {
            throw new PersistenceException("Registro não encontrado!");
        }

    }

    /**
     * 
     * Buscar um entrevistado a partir de um e-mail informado por parâmetro e considerando que o cadastro do entrevistado está válido.
     * 
     * @param email e-mail para busca.
     * @return entrevistado associado ao e-mail informado.
     * @throws PersistenceException 
     */
    public Entrevistado buscarPorEmailValido(String email) throws PersistenceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        String hql = "from entrevistado as ent where ent.email ='" + email + "' and ent.validado='Y'";
        Query query = session.createQuery(hql);
        List results = query.list();
        session.getTransaction().commit();
        session.close();
        if (!results.isEmpty()) {
            return (Entrevistado) results.get(0);
        } else {
            throw new PersistenceException("O e-mail não encontra-se cadastrado!");
        }

    }

    /**
     * 
     * Buscar um entrevistado por e-mail e senha considerando que o entrevistado está com cadastro válido.
     * 
     * @param email e-mail para busca.
     * @param senha senha para busca.
     * @return entrevistado associado com o e-mail e a senha dados como parâmetro.
     * @throws LoginException 
     */
    public Entrevistado buscarPorEmailSenhaValido(String email, String senha) throws LoginException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        String hql = "from entrevistado as ent where ent.email ='" + email + "' and ent.senha='" + senha + "' and ent.validado='Y'";
        Query query = session.createQuery(hql);
        List results = query.list();
        session.getTransaction().commit();
        session.close();
        if (!results.isEmpty()) {
            return (Entrevistado) results.get(0);
        } else {
            throw new LoginException("Acesso negado!");
        }

    }
    
    /**
     * 
     * Buscar entrevistados por campus associado.
     * 
     * @param campus campus ao qual o entrevistado está associado.
     * @return lista de entrevistados associados ao campus dado como parâmetro.
     */
    public List<Entrevistado> buscarPorCampus(Campus campus){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        String hql = "from entrevistado as ent where ent.campus.idCampus =" + campus.getIdCampus();
        Query query = session.createQuery(hql);
        List results = query.list();
        session.getTransaction().commit();
        session.close();
        return results;
    }

}