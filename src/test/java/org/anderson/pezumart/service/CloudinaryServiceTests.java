package org.anderson.pezumart.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.cloudinary.Url;
import org.anderson.pezumart.service.impl.CloudinaryServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CloudinaryServiceTests {

    @Mock
    private Cloudinary cloudinary;

    @InjectMocks
    private CloudinaryServiceImpl cloudinaryService;

    @Test
    @DisplayName("Subir imagen con Cloudinary")
    void CloudinaryService_UploadImage_ReturnMap() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());
        HashMap<Object, Object> options = new HashMap<>();
        String publicIdOption = file.getName() + "_" + UUID.randomUUID();

        options.put("folder", "test");
        options.put("public_id", publicIdOption);

        // Mock de Uploader
        Uploader uploaderMock = Mockito.mock(Uploader.class);

        when(cloudinary.uploader())
                .thenReturn(uploaderMock);
        when(uploaderMock.upload(eq(file.getBytes()), any(Map.class))).thenReturn(options);

        // Mock de Url
        Url urlMock = Mockito.mock(Url.class);
        when(cloudinary.url()).thenReturn(urlMock);
        when(urlMock.secure(true)).thenReturn(urlMock);
        when(urlMock.generate(Mockito.anyString()))
                .thenReturn("http://test.com/test.jpg");

        Map<String, String> imagenSubida = cloudinaryService.uploadImage(file, "test");

        Assertions.assertThat(imagenSubida).isNotNull();
        Assertions.assertThat(imagenSubida).containsKeys("publicId", "url");
        Assertions.assertThat(imagenSubida.get("url")).isEqualTo("http://test.com/test.jpg");
    }

    @Test
    @DisplayName("Eliminar imagen con Cloudinary")
    void CloudinaryService_EliminarImagen_ReturnVoid() throws IOException {

        Uploader uploaderMock = Mockito.mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(uploaderMock);

        when(uploaderMock.destroy(Mockito.anyString(), Mockito.anyMap()))
                .thenReturn(Map.of("result", "ok"));

        cloudinaryService.deleteImage("test");

        verify(uploaderMock).destroy("test", Map.of(
                "resource_type", "image",
                "invalidate", true
        ));
    }
}
