/*
 *  Copyright (c) 2020, 2021 Microsoft Corporation
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Microsoft Corporation - initial API and implementation
 *
 */

package org.eclipse.dataspaceconnector.spi.transfer.store;

import org.eclipse.dataspaceconnector.spi.persistence.StateEntityStore;
import org.eclipse.dataspaceconnector.spi.query.QuerySpec;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

/**
 * Manages persistent storage of {@link TransferProcess} state.
 */
public interface TransferProcessStore extends StateEntityStore<TransferProcess> {

    /**
     * compute an externalized key for a given transfer process
     *
     * @param process whose external key should be generated
     * @return externallized key
     */
    static String getExternalKey(TransferProcess process) {
        return getExternalKey(process.getType(), process.getDataRequest().getId());
    }

    /**
     * compute an externalized key
     *
     * @param type of the transfer process
     * @param transferId of the transfer process
     * @return externalized key
     */
    static String getExternalKey(TransferProcess.Type type, String transferId) {
        return type + "." + transferId;
    }

    /**
     * Returns the transfer process for the id or null if not found.
     */
    @Nullable
    TransferProcess find(String id);

    /**
     * Returns the transfer process for the data request id or null if not found.
     */
    @Nullable
    String processIdForTransferId(String id);

    /**
     * Creates a transfer process.
     */
    void create(TransferProcess process);

    /**
     * Updates a transfer process.
     */
    void update(TransferProcess process);

    /**
     * Deletes a transfer process.
     */
    void delete(String processId);

    /**
     * Returns all the transfer processes in the store that are covered by a given {@link QuerySpec}.
     * <p>
     * Note: supplying a sort field that does not exist on the {@link TransferProcess} may cause some implementations
     * to return an empty Stream, others will return an unsorted Stream, depending on the backing storage implementation.
     */
    Stream<TransferProcess> findAll(QuerySpec querySpec);
}
