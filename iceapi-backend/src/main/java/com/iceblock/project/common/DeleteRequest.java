package com.iceblock.project.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求
 *
 * @author zhang.
 */
@Data
public class DeleteRequest implements Serializable {
    /**
     * id
     */
    private Long[] ids;

    private static final long serialVersionUID = 1L;
}