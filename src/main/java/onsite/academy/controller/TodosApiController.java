package onsite.academy.controller;

import onsite.academy.model.Todo;
import onsite.academy.api.TodosApi;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;

@RestController
@RequestMapping("/")
public class TodosApiController implements TodosApi {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TodosApiController.class);

    public ResponseEntity<List<Todo>> todosGet() {
        LOGGER.info("Hello from doit2day-service!\nThis log message was produced at {}#todosGet", getClass().getCanonicalName());

        int httpStatusCode = 200;
        return ResponseEntity.status(httpStatusCode).build();
    }

    public ResponseEntity<Void> todosIdDelete(@Parameter(name = "id", description = "", required = true, in = ParameterIn.PATH) @PathVariable("id") Integer id) {
        LOGGER.info("Hello from doit2day-service!\nThis log message was produced at {}#todosIdDelete", getClass().getCanonicalName());

        int httpStatusCode = 204;
        return ResponseEntity.status(httpStatusCode).build();
    }

    public ResponseEntity<Todo> todosIdGet(@Parameter(name = "id", description = "", required = true, in = ParameterIn.PATH) @PathVariable("id") Integer id) {
        LOGGER.info("Hello from doit2day-service!\nThis log message was produced at {}#todosIdGet", getClass().getCanonicalName());

        int httpStatusCode = 200;
        return ResponseEntity.status(httpStatusCode).build();
    }

    public ResponseEntity<Void> todosIdPut(@Parameter(name = "id", description = "", required = true, in = ParameterIn.PATH) @PathVariable("id") Integer id, @Parameter(name = "Todo", description = "", required = true) @Valid @RequestBody Todo todo) {
        LOGGER.info("Hello from doit2day-service!\nThis log message was produced at {}#todosIdPut", getClass().getCanonicalName());

        int httpStatusCode = 200;
        return ResponseEntity.status(httpStatusCode).build();
    }

    public ResponseEntity<Void> todosPost(@Parameter(name = "Todo", description = "", required = true) @Valid @RequestBody Todo todo) {
        LOGGER.info("Hello from doit2day-service!\nThis log message was produced at {}#todosPost", getClass().getCanonicalName());

        int httpStatusCode = 201;
        return ResponseEntity.status(httpStatusCode).build();
    }
}
