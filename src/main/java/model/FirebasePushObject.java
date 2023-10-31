package model;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.CountDownLatch;

public class FirebasePushObject {
    
    private FirebaseDatabase firebaseDatabase;

    /**
     * Inicialización de Firebase.
     */
    private void initFirebase() {
        try {
            FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                    .setDatabaseUrl("https://prueba-esp-a7c2a.firebaseio.com")
                    .setServiceAccount(new FileInputStream(new File("C:\\Users\\Daniel\\Documents\\NetBeansProjects\\firebase\\prueba2-97bb1-firebase-adminsdk-mrv9r-e6d3c696cf.json")))
               //    .setServiceAccount(new FileInputStream(new File("C:\\Users\\Lenovo\\Documents\\pc\\NetBeansProjects\\firebase\\prueba-esp-a7c2a-firebase-adminsdk-yd7qe-1bdb100458.json"))) 
                    .build();

            FirebaseApp.initializeApp(firebaseOptions);
            firebaseDatabase = FirebaseDatabase.getInstance();
            System.out.println("La conexión se realizó exitosamente..."); //Para verificar conexión
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void saveItem(Item item) {
        if (item != null) {  //Si hay un dato guardado en espacio de memoria distinto a null
            initFirebase();
            
            DatabaseReference databaseReference = firebaseDatabase.getReference("/");
          
            DatabaseReference childReference = databaseReference.child("CarpetaPrueba");
           
            CountDownLatch countDownLatch = new CountDownLatch(1);            
            childReference.push().setValue(item, new DatabaseReference.CompletionListener() {

                @Override
                public void onComplete(DatabaseError de, DatabaseReference dr) {
                    System.out.println("Registro guardado!");
                    countDownLatch.countDown();
                }
            });
            try {
                countDownLatch.await();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void updateItem(String itemName, Item updatedItem) {
        if (itemName != null && updatedItem != null) {
            initFirebase();
            
            DatabaseReference databaseReference = firebaseDatabase.getReference("/");
            DatabaseReference childReference = databaseReference.child("CarpetaPrueba");

            Query query = childReference.orderByChild("name").equalTo(itemName);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            snapshot.getRef().setValue(updatedItem, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError de, DatabaseReference dr) {
                                    System.out.println("Registro actualizado!");
                                }
                            });
                        }
                    } else {
                        System.out.println("No se encontró ningún registro con el nombre: " + itemName);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("Error al buscar el registro: " + databaseError.getMessage());
                }
            });
        }
    }

    public void findItemByName(String itemName) {
        if (itemName != null) {
            initFirebase();
            
            DatabaseReference databaseReference = firebaseDatabase.getReference("/");
            DatabaseReference childReference = databaseReference.child("CarpetaPrueba");

            Query query = childReference.orderByChild("name").equalTo(itemName);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Item item = snapshot.getValue(Item.class);
                            System.out.println("Registro encontrado: " + item.toString());
                        }
                    } else {
                        System.out.println("No se encontró ningún registro con el nombre: " + itemName);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("Error al buscar el registro: " + databaseError.getMessage());
                }
            });
        }
    }

    public static void main(String[] args) {
        FirebasePushObject firebasePushObject = new FirebasePushObject(); //Creo un objeto
        
        Scanner scanner = new Scanner(System.in); //Para pedir datos por consola
        
        
        System.out.print("Ingrese el Nombre: ");
        String name = scanner.nextLine(); //Me pide ingresar un dato y luego se sigue ejecutando el codigo
        
        System.out.print("Ingrese el Precio: ");
        double price = scanner.nextDouble(); //Me pide ingresar un dato y luego se sigue ejecutando el codigo
        
        Item item = new Item();
        item.setId(1L);
        item.setName(name);
        item.setPrice(price);
        
        // Guardar el item
        firebasePushObject.saveItem(item);
        
        // Consultar por nombre
        System.out.print("Ingrese el nombre a buscar: ");
        String searchName = scanner.next();
        firebasePushObject.findItemByName(searchName);
        
        // Actualizar registro
        System.out.print("Ingrese el nuevo precio: ");
        double newPrice = scanner.nextDouble();
        
       /*Item updatedItem = new Item();
        updatedItem.setId(1L);
        updatedItem.setName(name);
        updatedItem.setPrice(newPrice);
        
        firebasePushObject.updateItem(name, updatedItem); */
    }
}