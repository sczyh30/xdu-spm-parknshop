package io.spm.parknshop.api.controller;

import io.spm.parknshop.api.util.AuthUtils;
import io.spm.parknshop.product.service.ProductService;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;

/**
 * Image upload controller.
 *
 * @author four
 * @author Eric Zhao
 */
@RestController
@RequestMapping("/api/v1/")
public class ImageUploadApiController {

  @Autowired
  private ProductService productService;

  @PostMapping(value = "/img/user_profile/upload_avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseBody
  public Publisher<?> apiUploadUserAvatar(ServerWebExchange exchange, @RequestBody Flux<Part> parts) {
    return AuthUtils.getUserId(exchange)
      .flatMapMany(userId -> parts.filter(e -> e instanceof FilePart)
        .map(e -> (FilePart) e)
        .flatMap(e -> saveImg(e, "user", String.format("user_avatar_%d.png", userId)))
      );
  }

  @PostMapping(value = "/img/seller_profile/upload_avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseBody
  public Publisher<?> apiUploadSellerAvatar(ServerWebExchange exchange, @RequestBody Flux<Part> parts) {
    return AuthUtils.getSellerId(exchange)
      .flatMapMany(sellerId -> parts.filter(e -> e instanceof FilePart)
        .map(e -> (FilePart) e)
        .flatMap(e -> saveImg(e, "seller", String.format("seller_avatar_%d.png", sellerId)))
      );
  }

  @PostMapping(value = "/img/ad/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseBody
  public Mono<?> apiUploadAdImg(@RequestBody Flux<Part> parts) {
    return parts.filter(e -> e instanceof FilePart)
      .map(e -> (FilePart) e)
      .take(1)
      .single()
      .flatMap(e -> saveImg(e, "advertisement", String.format("ad_%d%04d.png", System.currentTimeMillis(), ThreadLocalRandom.current().nextInt(0, 9999))));
  }

  @PostMapping(value = "/img/product/upload/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseBody
  public Mono<?> apiUploadProductImg(@PathVariable("id") String id, @RequestBody Flux<Part> parts) {
    return parts.filter(e -> e instanceof FilePart)
      .map(e -> (FilePart) e)
      .take(1)
      .single()
      .flatMap(e -> saveImg(e, "product", String.format("product_%s.jpg", id)))
      .flatMap(r -> productService.modifyPicUrl(r, Long.valueOf(id)));
  }

  @PostMapping(value = "/img/store/upload/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseBody
  public Publisher<?> apiUploadStoreImg(@PathVariable("id") String id, @RequestBody Flux<Part> parts) {
    return parts.filter(e -> e instanceof FilePart)
      .map(e -> (FilePart) e)
      .flatMap(e -> saveImg(e, "store", String.format("store_%s.png", id)));
  }

  private Mono<String> saveImg(FilePart part, String dir, String filename) {
    return createFile(part, dir, filename)
      .flatMap(part::transferTo)
      .map(v -> filename)
      .switchIfEmpty(Mono.just(filename));
  }

  private Mono<File> createFile(FilePart part, String dir, String filename) {
    return async(() -> {
      File file = new File("img/" + dir + "/", filename);
      if (file.exists()) {
        file.delete();
      }
      File parent = file.getParentFile();
      if (!parent.exists()) {
        parent.mkdirs();
      }
      file.createNewFile();
      return file;
    });
  }
}
