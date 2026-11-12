package mx.gob.tabasco.cmaig.sigtic.sac.pojo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectorioContactoTest {
    @Test
    void scalarProjectionKeepsPublicContractAndNormalizesNamesWithoutChangingInstitutionalValues() {
        DirectorioContacto contact = new DirectorioContacto(1L, "  Ána ", " María ", "López", null, "Analista",
                21L, "Ente", 11L, "Unidad", "00993", "001", "contacto@example.test", "HIS");
        JsonNode json = new ObjectMapper().valueToTree(contact);
        assertEquals(11, json.size());
        assertEquals("Ána María López", json.path("nombreCompleto").asText());
        assertEquals("00993", json.path("telefono").asText());
        assertEquals("001", json.path("extension").asText());
        assertEquals("HIS", json.path("estado").asText());
        assertFalse(json.has("rfc"));
        assertFalse(json.has("curp"));
        assertFalse(json.has("password"));
    }

    @Test
    void scalarProjectionPreservesMissingInstitutionalData() {
        DirectorioContacto contact = new DirectorioContacto(1L, null, null, null, null, null,
                null, null, null, null, null, null, null, "VIG");
        assertEquals("", contact.getNombreCompleto());
        assertNull(contact.getIdUnidadAdministrativa());
        assertNull(contact.getUnidadAdministrativa());
        assertNull(contact.getCorreo());
        assertNull(contact.getTelefono());
    }
}
