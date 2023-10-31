
package modelview;

public abstract class element {
    
    protected String nombre;
    protected String referencia;
    protected String precio;
    protected String cantidad;
    protected String sección;               //Son los atributos en común para productos 
                                            //guardados en bodega y los que se van a agregar.

    public element(String nombre, String referencia, String precio, String cantidad, String sección) {
        this.nombre = nombre;
        this.referencia = referencia;
        this.precio = precio;
        this.cantidad = cantidad;
        this.sección = sección;
    }
    
    
    
    
    public abstract int mostrarProducto();
     
    
    
}
