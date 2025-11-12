package com.mycompany.myapp.service.session;

import java.io.Serializable;

public record AsientoTemporal(
    int fila,
    int columna
) implements Serializable {
}
