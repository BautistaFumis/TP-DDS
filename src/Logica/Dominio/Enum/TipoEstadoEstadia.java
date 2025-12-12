package Logica.Dominio.Enum;

public enum TipoEstadoEstadia {
    RESERVADA,  // <-- NUEVO: Indica que es una reserva futura, aun no hubo check-in
    ACTIVA,     // La gente está en el hotel
    CERRADA,    // Ya se fueron (Checkout)
    CANCELADA,
}