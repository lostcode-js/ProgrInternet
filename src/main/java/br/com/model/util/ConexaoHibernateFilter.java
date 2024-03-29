package br.com.model.util;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.hibernate.SessionFactory;

public class ConexaoHibernateFilter implements Filter {
	
	private SessionFactory sf;
    @Override
    public void init(FilterConfig config) throws ServletException {
        try {
			this.sf = HibernateUtil.getSessionFactory();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @Override
    public void doFilter(ServletRequest servletRequest,
            ServletResponse servletResponse, 
            FilterChain chain) throws ServletException{
            try{
                this.sf.getCurrentSession().beginTransaction();
                chain.doFilter(servletRequest, servletResponse);
                this.sf.getCurrentSession().getTransaction().commit();
                this.sf.getCurrentSession().close();
            } catch (Throwable ex){
                try{
                    if(this.sf.getCurrentSession().getTransaction().isActive()){
                       this.sf.getCurrentSession().getTransaction().rollback();
                    }
                } catch (Throwable t){
                    t.printStackTrace();
                }
                throw new ServletException(ex);
            }
    }
    @Override
    public void destroy(){}
    public SessionFactory getSf() {
        return sf;
    }
    public void setSf(SessionFactory sf) {
        this.sf = sf;
    }

}
