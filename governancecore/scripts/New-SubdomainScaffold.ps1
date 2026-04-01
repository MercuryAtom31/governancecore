param(
    [Parameter(Mandatory = $true)]
    [string]$SubdomainName,

    [string]$BasePackage = "com.benzair.governancecore",

    [string]$SourceRoot = "src/main/java"
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

function Convert-ToPascalCase {
    param(
        [Parameter(Mandatory = $true)]
        [string]$Value
    )

    $parts = @($Value -split '[-_\s]+' | Where-Object { $_ -and $_.Trim().Length -gt 0 })
    if ($parts.Count -eq 0) {
        throw "SubdomainName must contain letters or numbers."
    }

    return ($parts | ForEach-Object {
        if ($_.Length -eq 1) {
            $_.ToUpperInvariant()
        }
        else {
            $_.Substring(0, 1).ToUpperInvariant() + $_.Substring(1).ToLowerInvariant()
        }
    }) -join ''
}

function New-JavaTypeFile {
    param(
        [Parameter(Mandatory = $true)]
        [string]$FilePath,

        [Parameter(Mandatory = $true)]
        [string]$PackageName,

        [Parameter(Mandatory = $true)]
        [string]$TypeKeyword,

        [Parameter(Mandatory = $true)]
        [string]$TypeName
    )

    if (Test-Path -LiteralPath $FilePath) {
        Write-Host "Skipped existing file: $FilePath"
        return
    }

    $content = @(
        "package $PackageName;",
        "",
        "public $TypeKeyword $TypeName {",
        "    ",
        "}"
    )

    Set-Content -LiteralPath $FilePath -Value $content -Encoding UTF8
    Write-Host "Created $TypeKeyword`: $FilePath"
}

$rootName = $SubdomainName.Trim().ToLowerInvariant()
if (-not $rootName.EndsWith("subdomain")) {
    $rootName = "$rootName" + "subdomain"
}

$entityName = $rootName.Substring(0, $rootName.Length - "subdomain".Length)
$entityName = Convert-ToPascalCase -Value $entityName

$subdomainRoot = Join-Path $SourceRoot (($BasePackage -replace '\.', '\') + "\" + $rootName)

$layout = @(
    @{ Layer = "presentationlayer"; Type = "class"; Name = "${entityName}Controller" },
    @{ Layer = "presentationlayer"; Type = "class"; Name = "${entityName}RequestModel" },
    @{ Layer = "presentationlayer"; Type = "class"; Name = "${entityName}ResponseModel" },
    @{ Layer = "businesslayer"; Type = "interface"; Name = "${entityName}Service" },
    @{ Layer = "businesslayer"; Type = "class"; Name = "${entityName}ServiceImpl" },
    @{ Layer = "datalayer"; Type = "class"; Name = $entityName },
    @{ Layer = "datalayer"; Type = "class"; Name = "${entityName}Identifier" },
    @{ Layer = "datalayer"; Type = "interface"; Name = "${entityName}Repository" },
    @{ Layer = "datamapperlayer"; Type = "class"; Name = "${entityName}RequestMapper" },
    @{ Layer = "datamapperlayer"; Type = "class"; Name = "${entityName}ResponseMapper" }
)

foreach ($item in $layout) {
    $layerPath = Join-Path $subdomainRoot $item.Layer
    if (-not (Test-Path -LiteralPath $layerPath)) {
        New-Item -ItemType Directory -Path $layerPath | Out-Null
        Write-Host "Created folder: $layerPath"
    }

    $packageName = "$BasePackage.$rootName.$($item.Layer)"
    $filePath = Join-Path $layerPath "$($item.Name).java"
    New-JavaTypeFile -FilePath $filePath -PackageName $packageName -TypeKeyword $item.Type -TypeName $item.Name
}

Write-Host ""
Write-Host "Scaffold completed for $rootName"
