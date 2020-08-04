package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
  private List<RsEvent> rsList = new ArrayList<RsEvent>();

  {
    rsList.add(new RsEvent("第一事件", "无分类"));
    rsList.add(new RsEvent("第二事件", "无分类"));
    rsList.add(new RsEvent("第三事件", "无分类"));
  }



  @GetMapping("/rs/{index}")
  public RsEvent getOneRsEvent(@PathVariable int index) {
    return rsList.get(index-1);
  }
}
