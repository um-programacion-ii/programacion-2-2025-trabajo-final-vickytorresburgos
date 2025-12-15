package com.mycompany.myapp.venta.domain.ports.in;

import com.mycompany.myapp.venta.domain.model.Venta;

public interface RealizarVentaUseCase {
    Venta realizarVenta(Venta venta);
}
