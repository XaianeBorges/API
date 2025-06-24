package com.flordacidade.api.flor_da_cidade_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    // ADICIONAR A CONSTANTE AQUI
    private static final String PLACEHOLDER_BANNER_FILENAME = "folhin.png";

    private final String hortaImageUploadDir;
    private final String bannerUploadDir;

    private Path hortaImageStorageLocation;
    private Path bannerStorageLocation;

    public FileStorageService(
            @Value("${file.upload-dir.hortas}") String hortaImageUploadDir,
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

    private String internalStoreFile(MultipartFile file, Path storageLocation) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = "";
        try {
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
        } catch (Exception e) {
            System.err.println("Erro ao obter extensão do arquivo: " + originalFileName + " - " + e.getMessage());
        }
        String newFileName = UUID.randomUUID().toString() + fileExtension;

        try {
            if (newFileName.contains("..")) {
                throw new RuntimeException("Nome de arquivo inválido (contém '..'): " + newFileName);
            }
            Path targetLocation = storageLocation.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return newFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Não foi possível armazenar o arquivo " + newFileName, ex);
        }
    }

    private void internalDeleteFile(String fileName, Path storageLocation) {
        // USA A CONSTANTE DEFINIDA NESTA CLASSE
        if (fileName == null || fileName.isBlank() || fileName.equals(PLACEHOLDER_BANNER_FILENAME)) {
            return;
        }
        try {
            Path filePath = storageLocation.resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            System.err.println("Não foi possível deletar o arquivo: " + fileName + " de " + storageLocation.toString() + " - " + ex.getMessage());
        }
    }

    public String storeHortaImage(MultipartFile file) {
        return internalStoreFile(file, this.hortaImageStorageLocation);
    }

    public void deleteHortaImage(String fileName) {
        internalDeleteFile(fileName, this.hortaImageStorageLocation);
    }

    public String storeBannerImage(MultipartFile file) {
        return internalStoreFile(file, this.bannerStorageLocation);
    }

    public void deleteBannerImage(String fileName) {
        // USA A CONSTANTE DEFINIDA NESTA CLASSE
        internalDeleteFile(fileName, this.bannerStorageLocation);
    }

    @Deprecated
    public String storeFile(MultipartFile file) {
        System.out.println("WARN: O método storeFile() está obsoleto. Usando storeHortaImage() como padrão para compatibilidade.");
        return storeHortaImage(file);
    }

    @Deprecated
    public void deleteFile(String fileName) {
        System.out.println("WARN: O método deleteFile() está obsoleto. Tentando deletar como imagem de banner por padrão.");
        // Usa a constante definida nesta classe
        internalDeleteFile(fileName, this.bannerStorageLocation);
    }
}