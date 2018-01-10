package io.spm.parknshop.apply.registry;

import io.spm.parknshop.apply.service.ApplyProcessService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplyProcessorRegistry {

  private final Map<Integer, ApplyProcessService> serviceMap = new ConcurrentHashMap<>();

}
