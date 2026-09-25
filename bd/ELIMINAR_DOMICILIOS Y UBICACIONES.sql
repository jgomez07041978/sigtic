declare
  v_cve_estatus varchar2(3);
  V_ID_DOMICILIO_ENTE_PUBLICO number;
begin
    for r in (select * from DOM_TEMPORAL) loop
    
    --select ID_DOMICILIO_ENTE_PUBLICO, cve_estatus into V_ID_DOMICILIO_ENTE_PUBLICO, v_cve_estatus from CAT_DOMICILIOS_ENTES_PUBLICOS de where de.ID_DOMICILIO_ENTE_PUBLICO=r.id_domicilio;
        
       /*
    for r2 in (select * from CAT_UBICACIONES_UNIDAD_ADMTVA ua where ua.ID_DOMICILIO_UNIDAD_ADMTVA=r.id_domicilio) loop            
    end loop;*/
        
      UPDATE CAT_UBICACIONES_UNIDAD_ADMTVA SET CVE_ESTATUS='NOV' WHERE ID_DOMICILIO_UNIDAD_ADMTVA=r.id_domicilio AND CVE_ESTATUS!='NOV' ;
        UPDATE CAT_DOMICILIOS_ENTES_PUBLICOS SET CVE_ESTATUS='NOV' WHERE ID_DOMICILIO_ENTE_PUBLICO=r.id_domicilio AND CVE_ESTATUS!='NOV';
        
        
        /*IF v_cve_estatus!='VIG' THEN
            dbms_output.put_line(V_ID_DOMICILIO_ENTE_PUBLICO);
            dbms_output.put_line(v_cve_estatus);
            
        
        END IF;*/
        

    end loop;    
    COMMIT;
end;