package neh.memocards.domain.converters;

import jakarta.persistence.AttributeConverter;
import neh.memocards.domain.entities.estudio.memocard.barajador.ITipoBarajador;

public class TipoBarajadorConverter implements AttributeConverter<ITipoBarajador, String> {
    @Override
    public String convertToDatabaseColumn(ITipoBarajador attribute) {
        return attribute.getClass().getName();
    }

    @Override
    public ITipoBarajador convertToEntityAttribute(String dbData) {
        try {
            return (ITipoBarajador) Class.forName(dbData).newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Error cargando barajador", e);
        }
    }
}
