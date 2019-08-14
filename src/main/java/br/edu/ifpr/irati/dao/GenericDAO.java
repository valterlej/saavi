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
import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * Classe concreta que implementa a interface Dao genérica.
 * 
 * @author Valter Estevam
 * @param <T>
 */
public class GenericDAO<T> implements Dao<T> {

    /**
     * Classe para a qual uma instância de objeto de acesso aos dados foi criada.
     */
    private final Class classePersistente;

    /**
     * 
     * @param classePersistente classe para a qual uma instância de objeto de acesso aos dados foi criada.
     */
    public GenericDAO(Class classePersistente) {
        this.classePersistente = classePersistente;
    }

    @Override
    public T buscarPorId(Serializable id) throws PersistenceException {
        Session session = null;
        T t = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            t = (T) session.get(classePersistente, id);
            session.close();
            return t;
        } catch (HibernateException | NullPointerException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void salvar(T t) throws PersistenceException {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(t);
            transaction.commit();
            session.close();
        } catch (HibernateException | NullPointerException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void alterar(T t) throws PersistenceException {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(t);
            transaction.commit();
            session.close();
        } catch (HibernateException | NullPointerException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void excluir(T t) throws PersistenceException {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.delete(t);
            transaction.commit();
            session.close();
        } catch (HibernateException | NullPointerException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public List<T> buscarTodos(Class<T> clazz) throws PersistenceException {        
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(clazz);
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            List<T> result = criteria.list();
            session.getTransaction().commit(); 
            session.close();
            return result;
        } catch (HibernateException | NullPointerException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

}
