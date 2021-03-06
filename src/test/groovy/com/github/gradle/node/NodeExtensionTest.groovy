package com.github.gradle.node

import com.github.gradle.AbstractProjectTest

class NodeExtensionTest extends AbstractProjectTest {
    def "check default values for extension"() {
        when:
        this.project.apply plugin: 'com.github.node-gradle.node'
        def ext = NodeExtension.get(this.project)

        then:
        ext != null
        ext.npmCommand == 'npm'
        ext.npxCommand == 'npx'
        ext.distBaseUrl == 'https://nodejs.org/dist'
        ext.workDir != null
        ext.nodeModulesDir != null
        ext.version == '12.16.1'
        !ext.download
        ext.npmVersion == ''
    }
}
