package filbehandling;

import javafx.concurrent.Task;
import produkter.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FiledataJOBJ extends Task<Void> {

    private Produkter produkter;
    private Path path;

    public void save(Produkter prod, Path path) throws IOException {
        try (OutputStream os = Files.newOutputStream(path);
             ObjectOutputStream out = new ObjectOutputStream(os)) {
            prod.writeObject(out);
        }
    }

    public void load(Produkter prod, Path path) throws Exception {
        try (InputStream is = Files.newInputStream(path);
             ObjectInputStream oin = new ObjectInputStream(is)) {
            prod.readObject(oin);
        }
    }


    public void setPath(Path path) {
        this.path = path;
    }

    public void setProdukt(Produkter produkter) {
        this.produkter = produkter;
    }


    @Override
    protected Void call() throws Exception {
        load(produkter, path);
        return null;
    }
}
