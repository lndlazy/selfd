package net.xnzn.app.selfdevice.init;

import java.util.Objects;

public class InitInfo {

   private boolean success;
   private String content;

   public InitInfo(boolean success, String content) {
      this.success = success;
      this.content = content;
   }

   public boolean isSuccess() {
      return success;
   }

   public void setSuccess(boolean success) {
      this.success = success;
   }

   public String getContent() {
      return content;
   }

   public void setContent(String content) {
      this.content = content;
   }


   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      InitInfo initInfo = (InitInfo) o;
      return success == initInfo.success && Objects.equals(content, initInfo.content);
   }

   @Override
   public int hashCode() {
      return Objects.hash(success, content);
   }
}
