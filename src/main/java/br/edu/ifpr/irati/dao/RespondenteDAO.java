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

import br.edu.ifpr.irati.exception.PersistenceException;
import br.edu.ifpr.irati.modelo.Campus;
import br.edu.ifpr.irati.modelo.Questionario;
import br.edu.ifpr.irati.modelo.Respondente;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * 
 * Classe para acesso aos dados de respondentes
 * 
 * Obs.: respondentes são registros de que um entrevistado respondeu a um questionário em uma data e hora específica.
 *
 * @author Valter Estevam
 */
public class RespondenteDAO {
    
    /**
     * Carrega todos os respondentes de um questionário.
     * 
     * @param questionario questionário selecionado.
     * @return listagem de respondentes.
     * @throws PersistenceException 
     */
    public List<Respondente> buscarPorQuestionario(Questionario questionario) throws PersistenceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        String hql = "from respondente as resp where resp.questionario.idQuestionario =" + questionario.getIdQuestionario();
        Query query = session.createQuery(hql);
        List results = query.list();
        session.getTransaction().commit();
        session.close();
        return results;
    }
    
    /**
     * Carrega todos os respondentes de um questionário provenientes de um campus específico.
     * 
     * @param questionario questionário selecionado.
     * @param campus campus selecionado.
     * @return listagem de respondentes.
     * @throws PersistenceException 
     */
    public List<Respondente> buscarPorQuestionarioCampus(Questionario questionario, Campus campus) throws PersistenceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        String hql = "from respondente as resp where resp.questionario.idQuestionario =" + questionario.getIdQuestionario()+" and resp.entrevistado.campus.idCampus = "+campus.getIdCampus();
        Query query = session.createQuery(hql);
        List results = query.list();
        session.getTransaction().commit();
        session.close();
        return results;
    }
    
}