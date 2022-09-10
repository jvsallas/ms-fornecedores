package br.com.mercadosallas.handler;

import br.com.mercadosallas.fornecedores.exception.FornecedorAlreadyExistsException;
import br.com.mercadosallas.fornecedores.exception.FornecedorNotFoundException;
import br.com.mercadosallas.handler.exceptions.dto.ErroDto;
import br.com.mercadosallas.handler.exceptions.dto.ErroFormularioDto;
import br.com.mercadosallas.telefones.exception.TelefoneNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ErrorInterceptorHandler {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErroFormularioDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<ErroFormularioDto> errosForm = new ArrayList<>();

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        fieldErrors.forEach(fieldError -> {
            String mensagem = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());

            ErroFormularioDto erroForm = new ErroFormularioDto(fieldError.getField(), mensagem);

            errosForm.add(erroForm);
        });

        return errosForm;
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DateTimeException.class)
    public String handleDateTimeException(DateTimeException exception) {
        return "Ocorreu um erro ao formatar a data";
    }

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErroDto handleException(Exception exception) {
        return new ErroDto("Ocorreu um inesperado: " + exception.getMessage());
    }

    @ResponseStatus(code = HttpStatus.CONFLICT)
    @ExceptionHandler(FornecedorAlreadyExistsException.class)
    public ErroDto handleFornecedorAlreadyExistsException(FornecedorAlreadyExistsException exception) {
        return new ErroDto(exception.getMessage());
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(FornecedorNotFoundException.class)
    public ErroDto handleFornecedorNotFoundException(FornecedorNotFoundException exception) {
        return new ErroDto(exception.getMessage());
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(TelefoneNotFoundException.class)
    public ErroDto handleTelefoneNotFoundException(TelefoneNotFoundException exception) {
        return new ErroDto(exception.getMessage());
    }

}
