package com.tuempresa.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;
import java.util.*;

@Service
public class ConsultaService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Map<String, Object> obtenerInformacion(String ruc, String placa) {
        String cacheKey = "consulta:" + ruc + ":" + placa;
        Object cache = redisTemplate.opsForValue().get(cacheKey);
        if (cache != null) return (Map<String, Object>) cache;

        Map<String, Object> datos = new HashMap<>();
        if (!consultarSRIExistencia(ruc)) {
            datos.put("error", "No es contribuyente registrado");
            return datos;
        }

        datos.put("sri", obtenerDatosSRI(ruc));
        datos.put("vehiculo", obtenerDatosVehiculo(placa));
        datos.put("licencia", simularANT(ruc, placa));

        redisTemplate.opsForValue().set(cacheKey, datos, Duration.ofHours(12));
        return datos;
    }

    private boolean consultarSRIExistencia(String ruc) {
        String url = "https://srienlinea.sri.gob.ec/sri-catastro-sujeto-servicio-internet/rest/ConsolidadoContribuyente/existePorNumeroRuc?numeroRuc=" + ruc;
        return Boolean.TRUE.equals(new RestTemplate().getForObject(url, Boolean.class));
    }

    private Map<String, Object> obtenerDatosSRI(String ruc) {
        String url = "https://srienlinea.sri.gob.ec/sri-catastro-sujeto-servicio-internet/rest/ConsolidadoContribuyente/obtenerPorNumerosRuc?&ruc=" + ruc;
        return new RestTemplate().getForObject(url, Map.class);
    }

    private Map<String, Object> obtenerDatosVehiculo(String placa) {
        String url = "https://srienlinea.sri.gob.ec/sri-matriculacion-vehicular-recaudacion-servicio-internet/rest/BaseVehiculo/obtenerPorNumeroPlacaOPorNumeroCampvOPorNumeroCpn?numeroPlacaCampvCpn=" + placa;
        return new RestTemplate().getForObject(url, Map.class);
    }

    private Map<String, Object> simularANT(String cedula, String placa) {
        Map<String, Object> simulado = new HashMap<>();
        simulado.put("estado", "Consulta ANT simulada debido a disponibilidad");
        return simulado;
    }
}
