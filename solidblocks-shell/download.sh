#!/usr/bin/env bash

: '
Downloads the file given by `${url}` to `${target_file}` and verifies if
the downloaded file has the checksum `${checksum}`. If a file is already
present at `${target}` download is skipped.
'
function download_get_and_verify_checksum {
    local url=${1}
    local target_file=${2}
    local checksum=${3}

    if [ -f "${target_file}" ]; then
        local target_file_checksum
        target_file_checksum=$(sha256sum ${target_file} | cut -d' ' -f1)
        if [ "${target_file_checksum}" == "${checksum}" ]; then
            echo "${url} already downloaded"
            return
        fi
    fi

    mkdir -p "$(dirname "${target_file}")" || true

    echo -n "downloading ${url}..."
    curl -sL "${url}" --output "${target_file}" > /dev/null
    echo "done"

    echo -n "verifying checksum..."
    echo "${checksum}" "${target_file}" | sha256sum --check --quiet
    echo "done"
}


: '
Extracts the file given by `${compressed_file}` to the directory `${target_dir}`.
Appropiate decompressor is chosen depending on file extension, currently `unzip`
for `*.zip` and `tar` for everything else. After uncompress a marker file is
written, indicating successful decompression. If this file is present when called
decompression is skipped.
'
function download_extract_file_to_directory {
    local compressed_file=${1}
    local target_dir=${2}

    local completion_marker=${target_dir}/.$(basename ${compressed_file}).extracted

    if [ -f "${completion_marker}" ]; then
        return
    fi

    mkdir -p "${target_dir}" || true

    echo -n "extracting ${compressed_file}..."
    if [[ ${compressed_file} =~ \.zip$ ]]; then
        unzip -qq -o "${compressed_file}" -d "${target_dir}"
        touch "${completion_marker}"
    else
        tar -xf "${compressed_file}" -C "${target_dir}"
        touch "${completion_marker}"
    fi

    echo "done"
}
