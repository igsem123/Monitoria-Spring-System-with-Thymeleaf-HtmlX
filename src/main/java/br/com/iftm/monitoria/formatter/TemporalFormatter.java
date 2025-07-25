package br.com.iftm.monitoria.formatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.Formatter;
import org.springframework.lang.NonNull;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Locale;

public abstract class TemporalFormatter<T extends Temporal> implements Formatter<T> {

    private static final Logger logger = LoggerFactory.getLogger(TemporalFormatter.class);

    @Override
    public @NonNull String print(@NonNull T temporal, @NonNull Locale locale) {
        logger.trace("Entrou em print");
        logger.debug("Objeto recebido: {}, Locale: {}", temporal, locale);
        DateTimeFormatter formatter = getDateTimeFormatter(locale);
        String retorno = formatter.format(temporal);
        logger.debug("String a retornar: {}", retorno);
        return retorno;
    }

    @Override
    public @NonNull T parse(@NonNull String text, @NonNull Locale locale) throws ParseException {
        logger.trace("Entrou em parse");
        logger.debug("String recebida: {}, Locale: {}", text, locale);
        DateTimeFormatter formatter = getDateTimeFormatter(locale);
        logger.debug("DateTimeFormatter: {}", formatter);
        T objeto = parse(text, formatter);
        logger.debug("Objeto a retornar: {}", objeto);
        return objeto;
    }

    private DateTimeFormatter getDateTimeFormatter(Locale locale) {
        String padrao = pattern(locale);
        logger.debug("Pattern: {}", padrao);
        return DateTimeFormatter.ofPattern(padrao);
    }

    public abstract String pattern(Locale locale);

    public abstract T parse(String text, DateTimeFormatter formatter);
}