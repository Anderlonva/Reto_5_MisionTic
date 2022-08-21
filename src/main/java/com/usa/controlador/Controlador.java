package com.usa.controlador;

import com.usa.modelo.Producto;
import com.usa.modelo.RepositorioProducto;
import com.usa.vista.VistaAPP;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;

/**
 *
 * @author Anderson Londoño
 */
public class Controlador implements ActionListener {

    RepositorioProducto repositorioProducto;
    VistaAPP vista;
    DefaultTableModel model;

    public Controlador() {
        super();

    }

    //se crea este constructoir recibiendo como parametros un objeto de tipo repositorio y uno de vista este se llama 
    //en el main
    public Controlador(RepositorioProducto repositorioProducto, VistaAPP vista) {
        this.repositorioProducto = repositorioProducto;
        this.vista = vista;
        agregarEventos();
        vista.setVisible(true);
        actualizarTabla();

    }

    private void agregarEventos() { // este metodo se pone en el constructor que se llama en el main
        vista.getBtn_agregar().addActionListener(this);
        vista.getBtn_actualizar().addActionListener(this);
        vista.getBtn_borrar().addActionListener(this);
        vista.getBtn_informes().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) { // este se usa para agregar el metodo al boton 
        if (e.getSource() == vista.getBtn_agregar()) {
            agregarProducto();
        }

        if (e.getSource() == vista.getBtn_actualizar()) {
            actualizarProducto();
        }

        if (e.getSource() == vista.getBtn_borrar()) {
            eliminarProducto();
        }

        if (e.getSource() == vista.getBtn_informes()) {
            JOptionPane.showMessageDialog(null, informes());
        }
    }

    public void agregarProducto() {
        try {
            if (validarTxts()) {
                Producto producto = new Producto(vista.getTxt_nombre().getText(),
                        Double.parseDouble(vista.getTxt_precio().getText()), Integer.parseInt(vista.getTxt_inventario().getText()));
                repositorioProducto.save(producto);
                JOptionPane.showMessageDialog(null, "El Producto se Agrego Correctamente");
                limpiarcajas();

            }
        } catch (DbActionExecutionException e) {

            JOptionPane.showMessageDialog(null, "El producto ya existe", "ERROR", JOptionPane.ERROR_MESSAGE);
            System.err.println(e);
            limpiarcajas();

        } finally {
            actualizarTabla();
        }

    }

    public void eliminarProducto() {

        Producto producto = new Producto(vista.getTxt_nombre().getText(),
                Double.parseDouble(vista.getTxt_precio().getText()), Integer.parseInt(vista.getTxt_inventario().getText()));

        List<Producto> listaProductos = (List<Producto>) repositorioProducto.findAll();

        try {
            if (validarTxts()) {

                for (Producto index : listaProductos) {
                    if (producto.getNombre().equals(index.getNombre())) {
                        repositorioProducto.delete(index);
                        JOptionPane.showMessageDialog(null, "El Producto se Elimino Correctamente");
                        limpiarcajas();
                    }
                }

            }

        } catch (DbActionExecutionException e) {
            JOptionPane.showMessageDialog(null, "El producto no existe", "ERROR", JOptionPane.ERROR_MESSAGE);
            System.err.println(e);
            limpiarcajas();

        } finally {

            actualizarTabla();
        }

    }

    public String informes() {

        String salida;

        salida = "El producto con el mayor precio es " + productoMayorPrecio() + ", el producto con el menor precio es "
                + productoMenorPrecio() + ", el promedio es " + String.format("%.1f", promedio()) + " y el total del inventario es "
                + informe1();

        return salida;
    }

    public void actualizarProducto() {

        String buscar = vista.getTxt_nombre().getText();  //

        List<Producto> listaProductos = (List<Producto>) repositorioProducto.findAll();

        try {
            if (validarTxts()) {

                for (Producto index : listaProductos) {
                    
                    if (buscar.equals(index.getNombre())) {
                        
                        int codigo = index.getCodigo();

                        Producto producto = new Producto(codigo, vista.getTxt_nombre().getText(),
                                Double.parseDouble(vista.getTxt_precio().getText()), Integer.parseInt(vista.getTxt_inventario().getText()));
                        
                        repositorioProducto.save(producto);
                        
                        JOptionPane.showMessageDialog(null, "El Producto se Actualizo Correctamente");
                        limpiarcajas();
                    }
                }

            }

        } catch (DbActionExecutionException e) {
            JOptionPane.showMessageDialog(null, "El producto no existe", "ERROR", JOptionPane.ERROR_MESSAGE);
            System.err.println(e);
            limpiarcajas();

        } finally {

            actualizarTabla();
        }

    }

    public void actualizarTabla() {

        String[] titulos = new String[]{"Nombre", "Precio", "Inventario"};
        model = new DefaultTableModel(titulos, 0);
        List<Producto> listaProductos = (List<Producto>) repositorioProducto.findAll();
        for (Producto index : listaProductos) {

            model.addRow(new Object[]{index.getNombre(), index.getPrecio(), index.getInventario()});

        }

        vista.getTabla_app().setModel(model);
        //vista.getTabla_app().setPreferredSize(new Dimension(350, model.getRowCount()*16)); // cada fila tendra 16 pixeles
        //vista.getjScrollPane1().setViewportView(vista.getTabla_app());
    }

    // validar los txt_Field que no esten vacios y sean numeros
    public boolean validarTxts() {
        if ("".equals(vista.getTxt_nombre().getText()) || "".equals(vista.getTxt_precio().getText())
                || "".equals(vista.getTxt_inventario().getText())) {
            JOptionPane.showMessageDialog(null, "Los campos de nombre, precio e inventario no pueden estar vacios",
                    "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!esNumero(vista.getTxt_precio().getText()) || !esNumero(vista.getTxt_inventario().getText())) {
            JOptionPane.showMessageDialog(null, "Los campos precio e inventario deben ser numeros",
                    "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // validar si es un numero
    public boolean esNumero(String numeroString) {
        try {
            double retorno = Double.parseDouble(numeroString);

        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public double informe1() {

        double total = 0;

        List<Producto> listaProductos = (List<Producto>) repositorioProducto.findAll();
        for (Producto producto : listaProductos) {
            total = total + (producto.getPrecio() * producto.getInventario());
        }

        return total;
    }

    public String productoMayorPrecio() {

        double precioMayor = 0;
        String nombre = "";
        List<Producto> listaProductos = (List<Producto>) repositorioProducto.findAll();
        for (Producto producto : listaProductos) {
            if (producto.getPrecio() > precioMayor) {
                nombre = producto.getNombre();
                precioMayor = producto.getPrecio();
            }
        }

        return nombre;
    }

    public String productoMenorPrecio() {

        String nombre = "";
        double precioMenor = 1000000;
        List<Producto> listaProductos = (List<Producto>) repositorioProducto.findAll();
        for (Producto producto : listaProductos) {
            if (producto.getPrecio() < precioMenor) {
                nombre = producto.getNombre();
                precioMenor = producto.getPrecio();
            }
        }

        return nombre;

    }

    public double promedio() {
        double suma = 0;
        List<Producto> listaProductos = (List<Producto>) repositorioProducto.findAll();
        for (Producto producto : listaProductos) {
            suma += producto.getPrecio();
        }
        return suma / (listaProductos.size());
    }

    public String Informe2() {
        String salida = "";
        // System.out.println(String.format("%.1f", informeInventario()));
        salida = productoMayorPrecio() + " " + productoMenorPrecio() + " " + String.format("%.1f", promedio());
        return salida;
    }

    void limpiarcajas() {

        vista.getTxt_nombre().setText(null);
        vista.getTxt_precio().setText(null);
        vista.getTxt_inventario().setText(null);

        vista.getTxt_nombre().requestFocus();
    }

}
