package com.example.ticketing.mbean;

public interface TicketingSystemAgentMBean {
    String[] listCategories();
    void setCategories(String[] categoryDefs);
    void setCategoryPriority(String categorySymbol, int priority);
    String[] getDeskCategories(int deskId);
    void setDeskCategories(int deskId, String csv);
    String[] listCategoryPriorities();
}
