package IntegracionBackFront.backfront.Services.Cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Service
public class CloudinaryService {

    //1. Definir el tamaño de las imagenes en MB
    private static  final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    //2. Definir las extensiones permitidas
    private  static  final String [] ALLOWERD_EXTENSIONS = {".jpg", ".jpeg", ".png"};
    //3. atributo claudinary
    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    //Subir imagenes a la raiz de Cloudinary
    public  String  uploadImage(MultipartFile file)throws IOException {
        validateImage(file);
        Map<?, ?> uploadResult = cloudinary.uploader().
                upload(file.getBytes(), ObjectUtils.asMap(
                        "resource_type", "auto",
                        "quality", "auto:good"
                ));
        return (String) uploadResult.get("secure_url");
    }


    private void validateImage(MultipartFile file) {
        //1. verificar si el archivo esta vacio
        if(file.isEmpty())
            throw new IllegalArgumentException("El archivo esta vacío.");

        //2. Verificar si el tamañ es adecuado
        if(file.getSize() > MAX_FILE_SIZE)
            throw new IllegalArgumentException("El tamaño del archivo debe ser de 5mb o menos");

        //3. Verificar el nombre original del archivo
        String originalFileName = file.getOriginalFilename();
        if(originalFileName == null) throw new IllegalArgumentException("nombre de archivo invalido");

        //4. Extraer y validar la extension del archivo
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
        if(!Arrays.asList(ALLOWERD_EXTENSIONS).contains(extension)) throw new IllegalArgumentException("solo se perimten archivos JPG, JPEG, PNG");

        //5. verificamos ue el tipo mime sea una imagen
        if (!file.getContentType().startsWith("image/")) throw new IllegalArgumentException("El Archivo debe ser una imagen valida");

    }
}
