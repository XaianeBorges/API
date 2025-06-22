package com.flordacidade.api.flor_da_cidade_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct; // Para Spring Boot 3+
// import javax.annotation.PostConstruct; // Para Spring Boot 2.x e Java EE

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    // Propriedades injetadas para os diferentes diretórios
    private final String hortaImageUploadDir;
    private final String bannerUploadDir;

    // Paths normalizados para os locais de armazenamento
    private Path hortaImageStorageLocation;
    private Path bannerStorageLocation;

    public FileStorageService(
            @Value("${file.upload-dir.hortas}") String hortaImageUploadDir, // Assumindo que './uploads/imagem' é para hortas
            @Value("${file.upload-dir.banners}") String bannerUploadDir) {
        this.hortaImageUploadDir = hortaImageUploadDir;
        this.bannerUploadDir = bannerUploadDir;
    }

    @PostConstruct
    public void init() {
        this.hortaImageStorageLocation = Paths.get(this.hortaImageUploadDir).toAbsolutePath().normalize();
        this.bannerStorageLocation = Paths.get(this.bannerUploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.hortaImageStorageLocation);
            Files.createDirectories(this.bannerStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Não foi possível criar o(s) diretório(s) de upload.", ex);
        }
    }

    // Método interno para a lógica de armazenamento, reutilizável
    private String internalStoreFile(MultipartFile file, Path storageLocation) {
        if (file == null || file.isEmpty()) {
            // Considerar lançar exceção ou retornar um valor que indique falha,
            // em vez de null, se um arquivo for esperado.
            // Para o caso de uma imagem ser opcional, null pode ser ok.
            return null;
        }
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = "";
        try {
            // Garante que originalFileName não é nulo antes de chamar lastIndexOf
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
        } catch (Exception e) {
            // Ignora se não houver extensão ou ocorrer outro erro
            System.err.println("Erro ao obter extensão do arquivo: " + originalFileName + " - " + e.getMessage());
        }
        String newFileName = UUID.randomUUID().toString() + fileExtension;

        try {
            if (newFileName.contains("..")) {
                throw new RuntimeException("Nome de arquivo inválido (contém '..'): " + newFileName);
            }
            Path targetLocation = storageLocation.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING); // Adicionado REPLACE_EXISTING
            return newFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Não foi possível armazenar o arquivo " + newFileName, ex);
        }
    }

    // Método interno para a lógica de deleção, reutilizável
    private void internalDeleteFile(String fileName, Path storageLocation) {
        if (fileName == null || fileName.isBlank()) {
            return;
        }
        try {
            Path filePath = storageLocation.resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            System.err.println("Não foi possível deletar o arquivo: " + fileName + " de " + storageLocation.toString() + " - " + ex.getMessage());
        }
    }

    // --- Métodos específicos para Imagens de Hortas ---
    public String storeHortaImage(MultipartFile file) {
        return internalStoreFile(file, this.hortaImageStorageLocation);
    }

    public void deleteHortaImage(String fileName) {
        internalDeleteFile(fileName, this.hortaImageStorageLocation);
    }

    // --- Métodos específicos para Banners ---
    public String storeBannerImage(MultipartFile file) {
        return internalStoreFile(file, this.bannerStorageLocation);
    }

    public void deleteBannerImage(String fileName) {
        internalDeleteFile(fileName, this.bannerStorageLocation);
    }


    // --- Métodos originais (agora podem ser delegados ou removidos) ---

    /**
     * @deprecated Use {@link #storeHortaImage(MultipartFile)} ou {@link #storeBannerImage(MultipartFile)} em vez disso.
     *             Este método agora delega para storeHortaImage por padrão.
     */
    @Deprecated
    public String storeFile(MultipartFile file) {
        // Por padrão, ou se este era o comportamento antigo principal, delegue para um deles
        System.out.println("WARN: O método storeFile() está obsoleto. Usando storeHortaImage() como padrão.");
        return storeHortaImage(file);
    }

    /**
     * @deprecated Use {@link #deleteHortaImage(String)} ou {@link #deleteBannerImage(String)} em vez disso.
     *             Este método agora delega para deleteHortaImage por padrão.
     */
    @Deprecated
    public void deleteFile(String fileName) {
        // Por padrão, ou se este era o comportamento antigo principal, delegue para um deles
        System.out.println("WARN: O método deleteFile() está obsoleto. Usando deleteHortaImage() como padrão.");
        deleteHortaImage(fileName);
    }
}