package io.spm.parknshop.api.controller;

import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;

/**
 * Img upload controller
 */
@RestController
@RequestMapping("/api/v1/")
public class ImageUploadApiController {

  @PostMapping(value = "/img/user/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseBody
  public Object apiUploadUserImg(@RequestBody Flux<Part> parts) {
    return parts.filter(e -> e instanceof FilePart)
        .map(e -> (FilePart) e)
        .flatMap(e -> saveImg(e, "user"));
  }

  @PostMapping(value = "/img/product/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseBody
  public Object apiUploadProductImg(@RequestBody Flux<Part> parts) {
    return parts.filter(e -> e instanceof FilePart)
        .map(e -> (FilePart) e)
        .flatMap(e -> saveImg(e, "product"));
  }

  @PostMapping(value = "/img/store/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseBody
  public Object apiUploadStoreImg(@RequestBody Flux<Part> parts) {
    return parts.filter(e -> e instanceof FilePart)
        .map(e -> (FilePart) e)
        .flatMap(e -> saveImg(e, "store"));
  }

  private Mono<String> saveImg(FilePart part, String dir) {
    //TODO Img name depends on username or productname
    return createFile(part, dir)
        .flatMap(part::transferTo)
        .map(e -> part.filename());
  }

  private Mono<File> createFile(FilePart part, String dir) {
    return async(() -> {
      File file = new File("img/" + dir + "/", "b.png");
      File parent = file.getParentFile();
      if (!parent.exists()) {
        parent.mkdirs();
      }
      file.createNewFile();
      return file;
    });
  }
}
