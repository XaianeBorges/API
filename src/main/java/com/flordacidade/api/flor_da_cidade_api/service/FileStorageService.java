package com.flordacidade.api.flor_da_cidade_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.util.StringUtils;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    // Injeta o valor da propriedade 'file.upload-dir' do application.properties
    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Não foi possível criar o diretório para armazenar os arquivos.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }

        // Gera um nome de arquivo único para evitar conflitos
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = "";
        try {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        } catch (Exception e) {
            // Ignora se não houver extensão
        }
        String newFileName = UUID.randomUUID().toString() + fileExtension;

        try {
            // Validações de segurança no nome do arquivo
            if (newFileName.contains("..")) {
                throw new RuntimeException("Nome de arquivo inválido: " + newFileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation);

            return newFileName;
        } catch (IOException ex) {
            throw new RuntimeException(
                    "Não foi possível armazenar o arquivo " + newFileName + ". Por favor, tente novamente!", ex);
        }
    }
}
