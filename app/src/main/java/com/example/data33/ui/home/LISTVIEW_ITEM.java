package com.example.data33.ui.home;

public class LISTVIEW_ITEM {

    private String strTitle ;
    private String strBook ;
    private String strDis ;
    private String strVac ;
    private String strAdd ;
    private String strDist ;
    private String strTotal ;


    public void setTitle(String title) {
        strTitle = title ;
    }
    public void setBook(String Book) {
        strBook = Book ;
    }
    public void setDis(String Dis) {
        strDis = Dis ;
    }
    public void setVac(String Vac) {
        strVac = Vac ;
    }
    public void setAdd(String Add) {
        strAdd = Add ;
    }
    public void setDist(String Dist) { strDist = Dist ;}
    public void setTotal(String Total) { strTotal = Total ;}


    public String getTitle() {
        return this.strTitle ;
    }
    public String getBook() { return this.strBook ; }
    public String getDis() {
        return this.strDis;
    }
    public String getVac() {
        return this.strVac ;
    }
    public String getAdd() {
        return this.strAdd ;
    }
    public String getDist() {
        return this.strDist ;
    }
    public String getTotal() { return this.strTotal ; }

}